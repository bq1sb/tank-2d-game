import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

    public class GameScreen extends JPanel {
        private GameMap gameMap;
        private PlayerTank playerTank;
        private JButton upButton, downButton, leftButton, rightButton;
        private Robot robot;
        private boolean gameOver = false; // Флаг Game Over
        private Point[] enemySpawnPoints = {
                new Point(2 * GameMap.TILE_SIZE, 1 * GameMap.TILE_SIZE),
                new Point(4 * GameMap.TILE_SIZE, 3 * GameMap.TILE_SIZE)
        };
        private int currentSpawnPointIndex = 0;

        public GameScreen() {
            setFocusable(true);
            setLayout(null);
            requestFocusInWindow();

            try {
                robot = new Robot();
            } catch (AWTException e) {
                System.err.println("Не удалось создать Robot: " + e.getMessage());
            }

            String[] level1 = {
                    "WWWWWWWWWWWWWWWWWWWWWWWWW",
                    "W.......................W",
                    "W..P....................W", // Линия разрушаемых стен
                    "W.......................W",
                    "W.......................W",
                    "W.......................W",
                    "W.......WW..............W",
                    "W........WW.............W",
                    "W.........WW............W",
                    "W.......................W",
                    "W.......................W",
                    "W.......................W",
                    "W.......................W",
                    "W.......................W",
                    "W.......................W",
                    "W...................E...W",
                    "W..................E....W",
                    "WWWWWWWWWWWWWWWWWWWWWWWWW"
            };
            playerTank = new PlayerTank(GameMap.TILE_SIZE * 2, GameMap.TILE_SIZE, null); // Начальное положение игрока
            gameMap = new GameMap(level1, playerTank); // Создаем GameMap, передавая данные
            playerTank.setWalls(gameMap.walls); // Теперь устанавливаем список стен

            int buttonWidth = 80;
            int buttonHeight = 60;
            int screenWidth = 800; // Увеличенный размер экрана
            int screenHeight = 600; // Увеличенный размер экрана
            int controlPanelHeight = 100;

            upButton = new JButton();
            downButton = new JButton();
            leftButton = new JButton();
            rightButton = new JButton();

            upButton.setBounds(screenWidth / 2 - buttonWidth / 2, screenHeight - controlPanelHeight - buttonHeight - 10, buttonWidth, buttonHeight);
            downButton.setBounds(screenWidth / 2 - buttonWidth / 2, screenHeight - controlPanelHeight + 10, buttonWidth, buttonHeight);
            leftButton.setBounds(screenWidth / 2 - buttonWidth - 50, screenHeight - controlPanelHeight - buttonHeight / 2, buttonWidth, buttonHeight);
            rightButton.setBounds(screenWidth / 2 + 50, screenHeight - controlPanelHeight - buttonHeight / 2, buttonWidth, buttonHeight);

            makeButtonInvisible(upButton);
            makeButtonInvisible(downButton);
            makeButtonInvisible(leftButton);
            makeButtonInvisible(rightButton);

            upButton.addActionListener(e -> pressKey(KeyEvent.VK_W));
            downButton.addActionListener(e -> pressKey(KeyEvent.VK_S));
            leftButton.addActionListener(e -> pressKey(KeyEvent.VK_A));
            rightButton.addActionListener(e -> pressKey(KeyEvent.VK_D));

            add(upButton);
            add(downButton);
            add(leftButton);
            add(rightButton);

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (!gameOver && playerTank.isAlive()) { // Добавлена проверка gameOver и жизни игрока
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_W -> playerTank.moveUp();
                            case KeyEvent.VK_S -> playerTank.moveDown();
                            case KeyEvent.VK_A -> playerTank.moveLeft();
                            case KeyEvent.VK_D -> playerTank.moveRight();
                            case KeyEvent.VK_SPACE -> playerTank.shoot();
                        }
                    }
                }
            });

            Timer gameTimer = new Timer(50, e -> updateGame());
            gameTimer.start();

            SwingUtilities.invokeLater(this::requestFocusInWindow);
        }

        private void pressKey(int keyCode) {
            if (robot != null && !gameOver && playerTank.isAlive()) { // Добавлена проверка gameOver и жизни игрока
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }
            requestFocusInWindow();
        }

        private void makeButtonInvisible(JButton button) {
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setText("");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (gameOver) {
                // Отображение Game Over
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("Game Over", getWidth() / 2 - 150, getHeight() / 2);
            } else {
                gameMap.draw(g);
                playerTank.draw(g);

                playerTank.getBullets().forEach(bullet -> bullet.draw(g));
                for (EnemyTank enemy : gameMap.enemies) {
                    if (enemy.isAlive()) {
                        enemy.draw(g);
                        enemy.getBullets().forEach(bullet -> bullet.draw(g));
                    }
                }
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString("Счёт: " + ScoreManager.getInstance().getScore(), 20, 30);
            }
        }

        public void updateGame() {
            if (!gameOver) {
                // Обновляем игрока
                playerTank.update();

                // Обновляем врагов, только если игрок жив
                if (playerTank.isAlive()) {
                    for (EnemyTank enemy : gameMap.enemies) {
                        enemy.update(getWidth(), getHeight());  // передаем размер экрана
                    }
                }

                // Проверка столкновений пуль игрока со стенами
                for (Bullet bullet : new ArrayList<>(playerTank.getBullets())) {
                    if (checkBulletWallCollision(bullet)) {
                        playerTank.getBullets().remove(bullet);
                    }
                }

                // Проверка столкновений пуль врагов со стенами
                for (EnemyTank enemy : gameMap.enemies) {
                    for (Bullet bullet : new ArrayList<>(enemy.getBullets())) {
                        if (checkBulletWallCollision(bullet)) {
                            enemy.getBullets().remove(bullet);
                        }
                    }
                }

                // Проверка столкновений пуль игрока с врагами
                // Проверка столкновений пуль игрока с врагами
                for (EnemyTank enemy : gameMap.enemies) {
                    for (Bullet bullet : new ArrayList<>(playerTank.getBullets())) {
                        if (enemy.isAlive() && bullet.getBounds().intersects(enemy.getBounds())) {
                            enemy.takeDamage();
                            playerTank.getBullets().remove(bullet);

                            // Проверка, уничтожен ли враг, и добавление очков
                            if (!enemy.isAlive()) {
                                ScoreManager.getInstance().addPoints(100); // Добавляем 100 очков за уничтожение врага
                            }
                        }
                    }
                }


                // Проверка столкновений пуль врагов с игроком
                for (EnemyTank enemy : gameMap.enemies) {
                    for (Bullet bullet : new ArrayList<>(enemy.getBullets())) {
                        if (playerTank.isAlive() && bullet.getBounds().intersects(playerTank.getBounds())) {
                            playerTank.takeDamage(1);  // Уменьшаем здоровье на 1
                            enemy.getBullets().remove(bullet);
                        }
                    }
                }

                // Респавн врагов (происходит независимо от состояния игрока)
                for (int i = 0; i < gameMap.enemies.size(); i++) {
                    EnemyTank enemy = gameMap.enemies.get(i);
                    if (!enemy.isAlive()) {
                        EnemyTank newEnemy = new EnemyTank(playerTank, gameMap.walls);
                        // Используем начальные позиции врагов из GameMap
                        if (gameMap.originalEnemyPositions != null && i < gameMap.originalEnemyPositions.size()) {
                            Point spawn = gameMap.originalEnemyPositions.get(i);
                            newEnemy.setPosition(spawn.x, spawn.y);
                            gameMap.enemies.set(i, newEnemy);
                        }
                    }
                }

                // Проверка Game Over
                if (!playerTank.isAlive() && !gameOver) {
                    gameOver = true;
                }

                repaint();
            }
        }

        private boolean checkBulletWallCollision(Bullet bullet) {
            Rectangle bulletRect = bullet.getBounds();
            for (Wall wall : gameMap.walls) {
                if (bulletRect.intersects(wall.getBounds())) {
                    return true;
                }
            }
            return false;
        }
    }
