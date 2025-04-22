import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerTank {
    private int x, y;
    private String direction;
    private List<Bullet> bullets;
    private int health = 5;

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

    public void moveUp() { y -= 3; direction = "UP"; }
    public void moveDown() { y += 3; direction = "DOWN"; }
    public void moveLeft() { x -= 3; direction = "LEFT"; }
    public void moveRight() { x += 3; direction = "RIGHT"; }

    public void shoot() {
        bullets.add(new Bullet(x + WIDTH / 2, y + HEIGHT / 2, direction));
    }

    public void update() {
        // Обновляем каждую пулю
        for (Bullet bullet : bullets) {
            bullet.update(800, 600); // Используем размеры экрана
        }

        // Удаляем неактивные пули
        bullets.removeIf(bullet -> !bullet.isActive());
    }


    public void draw(Graphics g) {
        if (isAlive()) {
            Image sprite = getCurrentSprite();
            if (sprite != null) g.drawImage(sprite, x, y, WIDTH, HEIGHT, null);
            else {
                g.setColor(Color.GREEN);
                g.fillRect(x, y, WIDTH, HEIGHT);
            }

            for (Bullet bullet : bullets) bullet.draw(g);

            drawHealthBar(g);
        }
    }

    private void drawHealthBar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y - 10, WIDTH, 5);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 10, (int)((health / 5.0) * WIDTH), 5);
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

    public int getX() {
        return x;
    }

    // Получаем координаты Y
    public int getY() {
        return y;
    }

    public List<Bullet> getBullets() { return bullets; }
    public Rectangle getBounds() { return new Rectangle(x, y, WIDTH, HEIGHT); }

    public void takeDamage() { health--; }
    public boolean isAlive() { return health > 0; }
}



















