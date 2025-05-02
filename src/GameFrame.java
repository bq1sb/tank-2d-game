import javax.swing.*;
import java.util.List;

public class GameFrame extends JFrame {

    private GamePanel gamePanel;
    private PlayerTank playerTank;
    private List<Wall> walls;
    private List<EnemyTank> enemies; // Список врагов
    private GameMap gameMap;

    public GameFrame() {
        setTitle("Tank Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Создаем игрока
        playerTank = new PlayerTank(100, 100, null); // Начальные координаты и список стен

        // Загружаем карту, создаем стены и врагов
        gameMap = new GameMap(GameMap.loadLevelData(1), playerTank);
        walls = gameMap.walls;
        enemies = gameMap.enemies; // Получаем список врагов из GameMap
        playerTank.setWalls(walls); // Устанавливаем стены игроку

        // Создаем GamePanel, передавая игрока, стены и список врагов
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
                    Thread.sleep(16); // ~60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


