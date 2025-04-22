import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int x, y;
    private final String direction;
    private final Image bulletImage;
    private boolean active = true; // Флаг для активности пули

    private static final int SPEED = 7;
    private static final int SIZE = 10;  // Размер пули

    public Bullet(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;

        // Загружаем изображение пули
        bulletImage = new ImageIcon(getClass().getResource("/bullet.png")).getImage();
    }

    // Обновление позиции пули
    public void update(int screenWidth, int screenHeight) {
        switch (direction) {
            case "UP" -> y -= SPEED;
            case "DOWN" -> y += SPEED;
            case "LEFT" -> x -= SPEED;
            case "RIGHT" -> x += SPEED;
        }

        if (isOutOfBounds(screenWidth, screenHeight)) {
            active = false;  // Деактивируем пулю, если она вышла за экран
        }
    }

    // Отрисовка пули
    public void draw(Graphics g) {
        if (!active) return;

        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, SIZE, SIZE, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, SIZE, SIZE);
        }
    }

    public boolean isOutOfBounds(int screenWidth, int screenHeight) {
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getDirection() { return direction; }
}









