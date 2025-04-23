import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {
    private static GameWindow instance;
    private GamePanel gamePanel;

    private GameWindow(PlayerTank playerTank, List<Wall> walls, List<EnemyTank> enemies) { // Изменили EnemyTank на List<EnemyTank>
        setTitle("Танчики");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(GamePanel.WIDTH, GamePanel.HEIGHT);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel(playerTank, walls, enemies); // Теперь передаем список врагов
        setContentPane(gamePanel);
        setVisible(true);

        // запуск игрового цикла
        new Thread(() -> {
            while (true) {
                gamePanel.update();
                try {
                    Thread.sleep(16); // 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static GameWindow getInstance(PlayerTank playerTank, List<Wall> walls, List<EnemyTank> enemies) { // Изменили EnemyTank на List<EnemyTank>
        if (instance == null) {
            instance = new GameWindow(playerTank, walls, enemies);
        }
        return instance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Инициализация игровых объектов
            List<Wall> gameWalls = new ArrayList<>();
            PlayerTank player = new PlayerTank(100, 100, gameWalls); // передаём стены в танк

            // Создаем список врагов (пока один)
            List<EnemyTank> enemiesList = new ArrayList<>();
            EnemyTank enemy = new EnemyTank(player, gameWalls);
            enemiesList.add(enemy);

            GameWindow.getInstance(player, gameWalls, enemiesList); // Передаем список врагов
        });
    }
}








