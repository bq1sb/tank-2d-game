import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JPanel {
    private GameMap gameMap;
    private PlayerTank playerTank;
    private JButton upButton, downButton, leftButton, rightButton;
    private JButton respawnButton;
    private Robot robot;
    private int currentLevel = 1;
    private boolean gameOver = false;
    private Image backgroundImage;
    private Image gameOverImage;
    private static final int GAME_OVER_IMAGE_WIDTH = 800;
    private static final int GAME_OVER_IMAGE_HEIGHT = 630;
    private List<Item> items = new ArrayList<>();

    public GameScreen() {
        setFocusable(true);
        setLayout(null);
        requestFocusInWindow();

        try {
            robot = new Robot();
            loadBackgroundImage(currentLevel);
            loadGameOverImage();
        } catch (AWTException e) {
            System.err.println("Не удалось создать Robot: " + e.getMessage());
        }

        loadLevel(currentLevel);

        int buttonWidth = 120;
        int buttonHeight = 60;
        int screenWidth = 800;
        int screenHeight = 600;
        int controlPanelHeight = 100;

        upButton = new JButton();
        downButton = new JButton();
        leftButton = new JButton();
        rightButton = new JButton();
        respawnButton = new JButton("Start Again");
        respawnButton.setFont(new Font("Arial", Font.BOLD, 16));
        respawnButton.setBounds(screenWidth / 2 - buttonWidth / 2, screenHeight / 2 + 80, buttonWidth, buttonHeight);
        respawnButton.setVisible(false);
        respawnButton.addActionListener(e -> restartGame());

        upButton.setBounds(screenWidth / 2 - 40, screenHeight - controlPanelHeight - 70, 80, 60);
        downButton.setBounds(screenWidth / 2 - 40, screenHeight - controlPanelHeight + 10, 80, 60);
        leftButton.setBounds(screenWidth / 2 - 130, screenHeight - controlPanelHeight - 30, 80, 60);
        rightButton.setBounds(screenWidth / 2 + 50, screenHeight - controlPanelHeight - 30, 80, 60);

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
        add(respawnButton);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver && playerTank.isAlive()) {
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

        Timer gameTimer = new Timer(50, this::updateGame);
        gameTimer.start();
        loadLevel(currentLevel);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void loadGameOverImage() {
        try {
            gameOverImage = ImageIO.read(getClass().getResource("/game_over.png"));
        } catch (IOException e) {
            System.err.println("Не удалось загрузить изображение 'Game Over': " + e.getMessage());
            gameOverImage = null;
        }
    }

    private void loadBackgroundImage(int level) {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/background" + level + ".png"));
        } catch (IOException e) {
            System.err.println("Не удалось загрузить фоновое изображение для уровня " + level + ": " + e.getMessage());
            backgroundImage = null;
        }
    }

    private void restartGame() {
        gameOver = false;
        currentLevel = 1;
        loadLevel(currentLevel);
        loadBackgroundImage(currentLevel);
        respawnButton.setVisible(false);
        requestFocusInWindow();
    }

    private void loadLevel(int levelNumber) {
        String[] levelData = GameMap.loadLevelData(levelNumber);
        playerTank = new PlayerTank(GameMap.TILE_SIZE * 2, GameMap.TILE_SIZE, null);
        gameMap = new GameMap(levelData, playerTank);
        playerTank.setWalls(gameMap.walls);
        ScoreManager.getInstance().reset();
        loadBackgroundImage(levelNumber);
        items.clear();
    }

    private void pressKey(int keyCode) {
        if (robot != null && !gameOver && playerTank.isAlive()) {
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

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(200, 200, 200));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        for (Item item : items) {
            item.draw(g);
        }

        if (gameOver) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            if (gameOverImage != null) {
                int imageX = getWidth() / 2 - GAME_OVER_IMAGE_WIDTH / 2;
                int imageY = getHeight() / 2 - GAME_OVER_IMAGE_HEIGHT / 2 - 30;
                g.drawImage(gameOverImage, imageX, imageY, GAME_OVER_IMAGE_WIDTH, GAME_OVER_IMAGE_HEIGHT, this);
            }
            respawnButton.setVisible(true);
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
            g.drawString("Уровень: " + currentLevel, 20, 60);
        }
    }

    public void updateGame(ActionEvent e) {
        if (!gameOver) {
            playerTank.update();

            if (playerTank.isAlive()) {
                for (EnemyTank enemy : gameMap.enemies) {
                    enemy.update(getWidth(), getHeight());
                }
            }

            if (playerTank.isAlive()) {
                List<Item> collectedItems = new ArrayList<>();
                for (Item item : items) {
                    if (!item.isCollected() && playerTank.getBounds().intersects(item.getBounds())) {
                        item.applyEffect(playerTank);
                        item.setCollected(true);
                        collectedItems.add(item);
                    }
                }
                items.removeAll(collectedItems);
            }

            List<Bullet> playerBulletsToRemove = new ArrayList<>();
            for (Bullet bullet : playerTank.getBullets()) {
                boolean bulletHitWall = false;
                for (Wall wall : new ArrayList<>(gameMap.walls)) {
                    if (bullet.getBounds().intersects(wall.getBounds())) {
                        playerBulletsToRemove.add(bullet);
                        bulletHitWall = true;
                        if (wall instanceof BrickWall) {
                            BrickWall brickWall = (BrickWall) wall;
                            brickWall.takeDamage();
                            if (brickWall.isDestroyed()) {
                                if (Math.random() < 0.5) {
                                    items.add(new Item(wall.getX() + GameMap.TILE_SIZE / 2 - 4, wall.getY() + GameMap.TILE_SIZE / 2 - 4));
                                }
                                gameMap.walls.remove(wall);
                            }
                        }
                        break;
                    }
                }
                if (bulletHitWall) {
                    continue;
                }
                for (EnemyTank enemy : gameMap.enemies) {
                    if (enemy.isAlive() && bullet.getBounds().intersects(enemy.getBounds())) {
                        enemy.takeDamage();
                        playerBulletsToRemove.add(bullet);
                        if (!enemy.isAlive()) {
                            ScoreManager.getInstance().addPoints(100);
                        }
                        break;
                    }
                }
            }
            playerTank.getBullets().removeAll(playerBulletsToRemove);

            List<Bullet> enemyBulletsToRemove = new ArrayList<>();
            for (EnemyTank enemy : gameMap.enemies) {
                for (Bullet bullet : enemy.getBullets()) {
                    boolean bulletHitWall = false;
                    for (Wall wall : new ArrayList<>(gameMap.walls)) {
                        if (bullet.getBounds().intersects(wall.getBounds())) {
                            enemyBulletsToRemove.add(bullet);
                            bulletHitWall = true;
                            if (wall instanceof BrickWall) {
                                BrickWall brickWall = (BrickWall) wall;
                                brickWall.takeDamage();
                                if (brickWall.isDestroyed()) {
                                    if (Math.random() < 0.5) {
                                        items.add(new Item(wall.getX() + GameMap.TILE_SIZE / 2 - 4, wall.getY() + GameMap.TILE_SIZE / 2 - 4));
                                    }
                                    gameMap.walls.remove(wall);
                                }
                            }
                            break;
                        }
                    }
                    if (bulletHitWall) {
                        continue;
                    }
                    if (playerTank.isAlive() && bullet.getBounds().intersects(playerTank.getBounds())) {
                        playerTank.takeDamage(1);
                        enemyBulletsToRemove.add(bullet);
                        break;
                    }
                }
                enemy.getBullets().removeAll(enemyBulletsToRemove);
            }

            int score = ScoreManager.getInstance().getScore();

            if (currentLevel == 1 && score >= 2000) {
                currentLevel = 2;
                loadLevel(currentLevel);
                loadBackgroundImage(currentLevel);
                playerTank.upgradeToLevel2();
            } else if (currentLevel == 2 && score >= 5000) {
                currentLevel = 3;
                loadLevel(currentLevel);
                loadBackgroundImage(currentLevel);
                playerTank.upgradeToLevel3();
            } else if (currentLevel == 3 && score >= 10000) {
                currentLevel = 4;
                loadLevel(currentLevel);
                loadBackgroundImage(currentLevel);
            } else if (currentLevel == 4 && score >= 20000) {
                currentLevel = 5;
                loadLevel(currentLevel);
                loadBackgroundImage(currentLevel);
            }


            for (int i = 0; i < gameMap.enemies.size(); i++) {
                EnemyTank enemy = gameMap.enemies.get(i);
                if (!enemy.isAlive()) {
                    EnemyTank newEnemy = new EnemyTank(playerTank, gameMap.walls);
                    if (gameMap.originalEnemyPositions != null && i < gameMap.originalEnemyPositions.size()) {
                        Point spawn = gameMap.originalEnemyPositions.get(i);
                        newEnemy.setPosition(spawn.x, spawn.y);
                        gameMap.enemies.set(i, newEnemy);
                    }
                }
            }

            if (!playerTank.isAlive() && !gameOver) {
                gameOver = true;
            }

            repaint();
        }
    }

}