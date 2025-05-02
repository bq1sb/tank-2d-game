import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class GamePanel extends JPanel {

    private PlayerTank playerTank;
    private List<EnemyTank> enemies; // Изменили на List
    private List<Wall> walls;
    private boolean gameOver = false; // Флаг для отслеживания состояния игры

    public static final int WIDTH = 40 * GameMap.TILE_SIZE; // 1280
    public static final int HEIGHT = 23 * GameMap.TILE_SIZE; // 736

    // Конструктор для инициализации панели игры
    public GamePanel(PlayerTank player, List<Wall> gameWalls, List<EnemyTank> enemiesList) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        this.playerTank = player;
        this.walls = gameWalls;
        this.enemies = enemiesList; // Принимаем список врагов
    }

    // Метод для завершения игры
    private void gameOver() {
        gameOver = true; // Устанавливаем флаг завершения игры в true

        // Показываем окно Game Over
        int option = JOptionPane.showOptionDialog(
                this,
                "Game Over!\nDo you want to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Start Again", "Exit"},
                "Start Again"
        );

        // Обработка выбора игрока
        if (option == JOptionPane.YES_OPTION) {
            // Запустить новую игру — перезапуск окна
            SwingUtilities.invokeLater(() -> {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.dispose(); // Закрыть текущее окно

                // Запуск новой игры
                new GameFrame();
            });
        } else {
            // Закрыть игру
            System.exit(0);
        }
    }

    // Метод обновления логики игры
    public void update() {
        // Если игра завершена, ничего не обновляем
        if (gameOver) {
            return;
        }

        // Обновление состояния игрока
        playerTank.update();

        // Обновление состояния вражеских танков
        for (EnemyTank enemy : enemies) {
            enemy.update(WIDTH, HEIGHT); // Обновляем каждый вражеский танк
        }

        // Проверка окончания игры (если здоровье игрока <= 0)
        if (playerTank.getHealth() <= 0) {
            gameOver();  // Завершаем игру
            return;  // Выход из метода, чтобы остановить обновление
        }

        // Перерисовка экрана (если игра не завершена)
        repaint();
    }

    // Метод для отрисовки экрана завершения игры
    private void showGameOverScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());  // Черный экран
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Game Over", getWidth() / 2 - 150, getHeight() / 2);  // Надпись "Game Over"
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Если игра завершена, показываем экран Game Over
        if (gameOver) {
            showGameOverScreen(g);
        } else {
            // Отрисовка игрока
            if (playerTank != null) {
                playerTank.draw(g);
            }

            // Отрисовка врагов
            for (EnemyTank enemy : enemies) {
                enemy.draw(g);
            }
        }
    }
}


















