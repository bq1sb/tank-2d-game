import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnemyTank {
    private int x, y;
    private int speed = 2;
    private int direction;
    private PlayerTank playerTank;

    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;

    private Image upSprite, downSprite, leftSprite, rightSprite;

    private List<Bullet> bullets = new ArrayList<>(); // Добавили список пуль

    public EnemyTank(int x, int y, PlayerTank playerTank) {
        this.x = x;
        this.y = y;
        this.playerTank = playerTank;
        this.direction = (int) (Math.random() * 4);  // Направление на старте случайное

        try {
            upSprite = ImageIO.read(getClass().getResource("/enemyTank_up.png"));
            downSprite = ImageIO.read(getClass().getResource("/enemyTank_down.png"));
            leftSprite = ImageIO.read(getClass().getResource("/enemyTank_left.png"));
            rightSprite = ImageIO.read(getClass().getResource("/enemyTank_right.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Не удалось загрузить спрайт: " + e.getMessage());
        }
    }

    public void update() {
        move();
        shoot();
        for (Bullet bullet : bullets) {
            bullet.update(); // Обновляем пули
        }
        // Удаляем пули, вышедшие за границы экрана
        bullets.removeIf(b -> b.isOutOfBounds(GamePanel.WIDTH, GamePanel.HEIGHT));
    }

    private void move() {
        switch (direction) {
            case 0 -> y -= speed;
            case 1 -> y += speed;
            case 2 -> x -= speed;
            case 3 -> x += speed;
        }

        if (y < 0 || y > GamePanel.HEIGHT - HEIGHT) direction = (int) (Math.random() * 4);
        if (x < 0 || x > GamePanel.WIDTH - WIDTH) direction = (int) (Math.random() * 4);
    }

    private void shoot() {
        if (Math.random() < 0.01) {
            Bullet bullet = createBullet();
            if (bullet != null) {
                bullets.add(bullet); // Добавляем пулю в список
            }
        }
    }

    private Bullet createBullet() {
        return switch (direction) {
            case 0 -> new Bullet(x + WIDTH / 2, y, "UP");
            case 1 -> new Bullet(x + WIDTH / 2, y + HEIGHT, "DOWN");
            case 2 -> new Bullet(x, y + HEIGHT / 2, "LEFT");
            case 3 -> new Bullet(x + WIDTH, y + HEIGHT / 2, "RIGHT");
            default -> null;
        };
    }

    public void draw(Graphics g) {
        Image sprite = getCurrentSprite();
        if (sprite != null) {
            g.drawImage(sprite, x, y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }

        // Отрисовка пуль
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    private Image getCurrentSprite() {
        return switch (direction) {
            case 0 -> upSprite;
            case 1 -> downSprite;
            case 2 -> leftSprite;
            case 3 -> rightSprite;
            default -> null;
        };
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}




