import java.awt.*;
public class Bullet {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private int x, y;
    private final String direction;
    private Image bulletImage;
    private boolean active = true;
    private static final int SPEED = 7;
    private static final int SIZE = 10;
    public Bullet(int x, int y, String direction, Image bulletImage) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.bulletImage = bulletImage;
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