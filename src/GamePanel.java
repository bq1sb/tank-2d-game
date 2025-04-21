import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    private PlayerTank playerTank;
    private EnemyTank enemyTank;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        // создание игрока
        playerTank = new PlayerTank(100, 100);

        // создание врага
        enemyTank = new EnemyTank(500, 300, playerTank);

        // обработка ввода клавиш
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // реагируем на нажатие клавиш
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> playerTank.moveUp();
                    case KeyEvent.VK_S -> playerTank.moveDown();
                    case KeyEvent.VK_A -> playerTank.moveLeft();
                    case KeyEvent.VK_D -> playerTank.moveRight();
                    case KeyEvent.VK_SPACE -> playerTank.shoot(); // Стрельба
                }
                repaint(); // перерисовываем панель после движения
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // можно оставить пустым
            }
        });

        requestFocusInWindow(); // для получения фокуса
    }

    // метод обновления логики игры
    public void update() {
        playerTank.update();
        enemyTank.update();
        repaint(); // перерисовываем экран
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // отрисовка игрока
        playerTank.draw(g);

        // отрисовка врага
        enemyTank.draw(g);

    }
}

















