import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Bullet {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private int x, y;
    private final String direction;
    private Image bulletImage;
    private boolean active = true;
    private boolean destroyed = false;

    private static final int SPEED = 7;
    private static final int SIZE = 10;  // Размер пули

    public Bullet(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        loadImage();
    }

    private void loadImage() {
        try {
            switch (direction) {
                case "UP" -> bulletImage = ImageIO.read(getClass().getResource("/bullet_up.png"));
                case "DOWN" -> bulletImage = ImageIO.read(getClass().getResource("/bullet_down.png"));
                case "LEFT" -> bulletImage = ImageIO.read(getClass().getResource("/bullet_left.png"));
                case "RIGHT" -> bulletImage = ImageIO.read(getClass().getResource("/bullet_right.png"));
            }
        } catch (IOException e) {
            System.err.println("Не удалось загрузить изображение пули: " + e.getMessage());
        }
    }

    // Обновление позиции пули
    public void update(int fieldWidth, int fieldHeight) {
        if (direction.equals("UP")) y -= SPEED;
        if (direction.equals("DOWN")) y += SPEED;
        if (direction.equals("LEFT")) x -= SPEED;
        if (direction.equals("RIGHT")) x += SPEED;

        if (x < 0 || x > fieldWidth || y < 0 || y > fieldHeight) {
            active = false;
        }
    }

    // Проверка на столкновения с стенами
    public boolean checkCollision(List<Wall> walls) {
        // Проверка столкновений с обычными стенами
        for (Wall wall : walls) {
            if (this.getBounds().intersects(wall.getBounds())) {
                System.out.println("Пуля столкнулась с обычной стеной!");
                active = false; // Пуля уничтожена при столкновении с обычной стеной
                return true;
            }
        }

        // Проверка столкновений с разрушаемыми стенами

        return false; // Нет столкновения
    }


    // Отрисовка пули
    public void draw(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, WIDTH, HEIGHT, null);
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

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void deactivate() {
        active = false;
    }
}
