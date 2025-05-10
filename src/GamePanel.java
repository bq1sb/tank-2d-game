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
    public GamePanel(PlayerTank player, List<Wall> gameWalls, List<EnemyTank> enemiesList) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        this.playerTank = player;
        this.walls = gameWalls;
        this.enemies = enemiesList;
    }

    private void gameOver() {
        gameOver = true;
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

    private void showGameOverScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Game Over", getWidth() / 2 - 150, getHeight() / 2);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            showGameOverScreen(g);
        } else {
            if (playerTank != null) {
                playerTank.draw(g);
            }
            for (EnemyTank enemy : enemies) {
                enemy.draw(g);
            }
        }
    }
}


















