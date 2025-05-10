import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;


public class Bullet {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private int x, y;
    private final String direction;
    private Image bulletImage;
    private boolean active = true;

    private static final int SPEED = 7;
    private static final int SIZE = 10;

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

    public void update(int fieldWidth, int fieldHeight) {
        if (direction.equals("UP")) y -= SPEED;
        if (direction.equals("DOWN")) y += SPEED;
        if (direction.equals("LEFT")) x -= SPEED;
        if (direction.equals("RIGHT")) x += SPEED;

        if (x < 0 || x > fieldWidth || y < 0 || y > fieldHeight) {
            active = false;
        }
    }

    public void draw(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, WIDTH, HEIGHT, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public boolean isActive() {
        return active;
    }
}
