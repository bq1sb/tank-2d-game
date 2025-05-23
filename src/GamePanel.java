import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {

    private PlayerTank playerTank;
    private List<EnemyTank> enemies;
    private List<Wall> walls;
    private boolean gameOver = false;

    public static final int WIDTH = 40 * GameMap.TILE_SIZE; // 1280
    public static final int HEIGHT = 23 * GameMap.TILE_SIZE; // 736

    private RenderStrategy currentRenderStrategy; // Поле для текущей стратегии отрисовки

    public GamePanel(PlayerTank player, List<Wall> gameWalls, List<EnemyTank> enemiesList) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        this.playerTank = player;
        this.walls = gameWalls;
        this.enemies = enemiesList;

        // Изначально устанавливаем стратегию для игрового процесса
        this.currentRenderStrategy = new GamePlayRenderStrategy(playerTank, enemies, walls);
    }

    private void gameOver() {
        gameOver = true;
        // При переходе в состояние Game Over, меняем стратегию отрисовки
        this.currentRenderStrategy = new GameOverRenderStrategy(this);

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

        if (option == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.dispose();
                new GameFrame();
            });
        } else {
            System.exit(0);
        }
    }

    public void update() {
        if (gameOver) {
            return;
        }
        playerTank.update();

        for (EnemyTank enemy : enemies) {
            enemy.update(WIDTH, HEIGHT);
        }
        if (playerTank.getHealth() <= 0) {
            gameOver();
            return;
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Теперь отрисовка делегируется текущей стратегии
        currentRenderStrategy.render(g);
    }
}