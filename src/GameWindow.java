import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private static GameWindow instance;
    private GamePanel gamePanel;

    private GameWindow() {
        setTitle("Танчики");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(GamePanel.WIDTH, GamePanel.HEIGHT);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        setContentPane(gamePanel);
        setVisible(true);

        // запуск игрового цикла
        new Thread(() -> {
            while (true) {
                gamePanel.update();
                try {
                    Thread.sleep(16); // пробуем приблизить 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static GameWindow getInstance() {
        if (instance == null) {
            instance = new GameWindow();
        }
        return instance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::getInstance);
    }
}







