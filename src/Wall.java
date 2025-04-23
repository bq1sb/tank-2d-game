import java.awt.*;
import java.util.Random;

public class Wall {
    private int x, y;
    private static final int WIDTH = GameMap.TILE_SIZE;
    private static final int HEIGHT = GameMap.TILE_SIZE;
    private static final Random RANDOM = new Random();

    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    // Рисуем стену с случайными трещинами
    public void draw(Graphics g) {
        g.setColor(new Color(139, 69, 19)); // Основной цвет стены
        g.fillRect(x, y, WIDTH, HEIGHT);
        g.setColor(new Color(101, 67, 33)); // Цвет для рамки
        g.drawRect(x, y, WIDTH, HEIGHT);

        // Логика для генерации трещин
        drawCracks(g);
    }

    private void drawCracks(Graphics g) {
        if (RANDOM.nextFloat() < 0.2) { // Вероятность появления трещины
            int crackX = x + RANDOM.nextInt(WIDTH);
            int crackY = y + RANDOM.nextInt(HEIGHT);
            g.drawLine(crackX, crackY, crackX + 5, crackY + 5);
        }

        if (RANDOM.nextFloat() < 0.1) { // Меньшая вероятность для второй трещины
            int crackX = x + RANDOM.nextInt(WIDTH);
            int crackY = y + RANDOM.nextInt(HEIGHT);
            g.drawLine(crackX, crackY + 3, crackX + 3, crackY - 2);
        }
    }
}







