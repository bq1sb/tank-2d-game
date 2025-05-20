import javax.swing.*;
import java.util.List;

public class GameFrame extends JFrame {

    private GamePanel gamePanel;
    private PlayerTank playerTank;
    private List<Wall> walls;
    private List<EnemyTank> enemies;
    private GameMap gameMap;
    private BulletFactory bulletFactory;

    public GameFrame() {
        setTitle("Tank Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        bulletFactory = new SimpleBulletFactory();

        // Передаем bulletFactory в конструктор PlayerTank
        playerTank = new PlayerTank(100, 100, null, bulletFactory); // <-- Вот здесь изменение
        gameMap = new GameMap(GameMap.loadLevelData(1), playerTank, bulletFactory);
        walls = gameMap.walls;
        enemies = gameMap.enemies;
        playerTank.setWalls(walls);
        gamePanel = new GamePanel(playerTank, walls, enemies);
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        startGameLoop(gamePanel);
    }

    private void startGameLoop(GamePanel gamePanel) {
        new Thread(() -> {
            while (true) {
                gamePanel.update();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}