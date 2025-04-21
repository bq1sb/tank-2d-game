import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerTank {
    private int x, y;
    private String direction;
    private List<Bullet> bullets;

    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;

    private Image upSprite, downSprite, leftSprite, rightSprite;

    public PlayerTank(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = "UP";
        this.bullets = new ArrayList<>();

        try {
            upSprite = ImageIO.read(getClass().getResource("/tank_up.png"));
            downSprite = ImageIO.read(getClass().getResource("/tank_down.png"));
            leftSprite = ImageIO.read(getClass().getResource("/tank_left.png"));
            rightSprite = ImageIO.read(getClass().getResource("/tank_right.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Не удалось загрузить спрайты танка: " + e.getMessage());
        }
    }

    public void moveUp() {
        y -= 2;
        direction = "UP";
    }

    public void moveDown() {
        y += 2;
        direction = "DOWN";
    }

    public void moveLeft() {
        x -= 2;
        direction = "LEFT";
    }

    public void moveRight() {
        x += 2;
        direction = "RIGHT";
    }

    public void shoot() {
        Bullet bullet = new Bullet(x + WIDTH / 2, y + HEIGHT / 2, direction);
        bullets.add(bullet);
    }

    public void update() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isOutOfBounds(GamePanel.WIDTH, GamePanel.HEIGHT)) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);
    }

    public void draw(Graphics g) {
        Image sprite = getCurrentSprite();
        if (sprite != null) {
            g.drawImage(sprite, x, y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.GREEN); // fallback если спрайт не загружен
            g.fillRect(x, y, WIDTH, HEIGHT);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    private Image getCurrentSprite() {
        return switch (direction) {
            case "UP" -> upSprite;
            case "DOWN" -> downSprite;
            case "LEFT" -> leftSprite;
            case "RIGHT" -> rightSprite;
            default -> null;
        };
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}


















