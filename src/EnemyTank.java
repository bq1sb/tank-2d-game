import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnemyTank {
    private int x, y;
    private int speed = 2;
    private int direction;
    private int health = 5;
    private PlayerTank playerTank;
    private List<Bullet> bullets = new ArrayList<>();
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private Image upSprite, downSprite, leftSprite, rightSprite;

    // Максимальное расстояние для начала стрельбы
    private static final double SHOOTING_DISTANCE = 200.0;
    private static final int FIRE_COOLDOWN = 1000; // Время между выстрелами (в миллисекундах)
    private long lastShootTime = 0;

    public EnemyTank(PlayerTank playerTank) {
        this.playerTank = playerTank;
        this.direction = (int) (Math.random() * 4);
        loadSprites();
        reset(); // Вызов для начальной позиции врага
    }

    private void loadSprites() {
        try {
            upSprite = ImageIO.read(getClass().getResource("/enemyTank_up.png"));
            downSprite = ImageIO.read(getClass().getResource("/enemyTank_down.png"));
            leftSprite = ImageIO.read(getClass().getResource("/enemyTank_left.png"));
            rightSprite = ImageIO.read(getClass().getResource("/enemyTank_right.png"));
        } catch (IOException e) {
            System.err.println("Не удалось загрузить спрайт врага: " + e.getMessage());
        }
    }

    public void reset() {
        int playerX = playerTank.getX();
        int playerY = playerTank.getY();

        if (playerY < 300) {
            this.x = (int) (Math.random() * 800);
            this.y = 600 - HEIGHT;
        } else {
            this.x = (int) (Math.random() * 800);
            this.y = 0;
        }

        this.health = 5;
        this.direction = (int) (Math.random() * 4);
    }

    public void update() {
        double distanceToPlayer = getDistanceToPlayer();
        if (distanceToPlayer < SHOOTING_DISTANCE) {
            if (canShoot()) {
                shoot();
            }
        } else {
            move();
        }

        for (Bullet bullet : bullets) {
            bullet.update(800, 600);
        }
        bullets.removeIf(bullet -> !bullet.isActive());
    }

    private double getDistanceToPlayer() {
        int dx = playerTank.getX() - this.x;
        int dy = playerTank.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void move() {
        if (playerTank != null && playerTank.isAlive()) {
            if (playerTank.getX() > x) { x += speed; direction = 3; }
            else if (playerTank.getX() < x) { x -= speed; direction = 2; }

            if (playerTank.getY() > y) { y += speed; direction = 1; }
            else if (playerTank.getY() < y) { y -= speed; direction = 0; }
        }
    }

    private boolean canShoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShootTime >= FIRE_COOLDOWN) {
            lastShootTime = currentTime;
            return true;
        }
        return false;
    }

    private void shoot() {
        if (Math.random() < 0.01) {
            bullets.add(createBullet());
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
        if (!isAlive()) return;

        Image sprite = getCurrentSprite();
        if (sprite != null) g.drawImage(sprite, x, y, WIDTH, HEIGHT, null);
        else {
            g.setColor(Color.RED);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }

        for (Bullet bullet : bullets) bullet.draw(g);

        drawHealthBar(g);
    }

    private void drawHealthBar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y - 10, WIDTH, 5);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 10, (int)((health / 5.0) * WIDTH), 5);
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

    public List<Bullet> getBullets() { return bullets; }
    public Rectangle getBounds() { return new Rectangle(x, y, WIDTH, HEIGHT); }

    public void takeDamage() { health--; }
    public boolean isAlive() { return health > 0; }

    public int getX() { return x; }
    public int getY() { return y; }
}













