import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        // настройки окна
        setTitle("Tank Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //панель игры
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        //размер окна
        pack();
        setLocationRelativeTo(null); // Центрируем окно
        setVisible(true);

        // запуск игрового цикла
        startGameLoop(gamePanel);
    }

    private void startGameLoop(GamePanel gamePanel) {
        new Thread(() -> {
            while (true) {
                gamePanel.update();  // обновление логики игры
                try {
                    Thread.sleep(16);  // 60к/с
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        // создаем и запускаем игровое окно
        SwingUtilities.invokeLater(GameFrame::new);
    }
}

