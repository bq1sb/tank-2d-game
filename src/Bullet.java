import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int x, y;
    private final String direction;
    private final Image bulletImage;

    private static final int SPEED = 7;
    private static final int SIZE = 10;  // Размер пули (ширина/высота)

    public Bullet(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;

        // Загружаем изображение пули
        bulletImage = new ImageIcon(getClass().getResource("/bullet.png")).getImage();
    }

    // Обновление позиции пули
    public void update() {
        switch (direction) {
            case "UP" -> y -= SPEED;     // Двигаем пулю вверх
            case "DOWN" -> y += SPEED;   // Двигаем пулю вниз
            case "LEFT" -> x -= SPEED;   // Двигаем пулю влево
            case "RIGHT" -> x += SPEED;  // Двигаем пулю вправо
        }
    }

    // Отрисовка пули
    public void draw(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, SIZE, SIZE, null);  // Рисуем пулю изображением
        } else {
            g.setColor(Color.RED);  // Если изображение не загружено, рисуем прямоугольник
            g.fillRect(x, y, SIZE, SIZE);
        }
    }

    // Проверка, вышла ли пуля за пределы экрана
    public boolean isOutOfBounds(int screenWidth, int screenHeight) {
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }

    // Для коллизий (если понадобится)
    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);  // Возвращаем прямоугольник для столкновений
    }

    // Геттеры
    public int getX() { return x; }
    public int getY() { return y; }
    public String getDirection() { return direction; }
}








