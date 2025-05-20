import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerTank {
    private int x, y;
    private Direction direction;
    private List<Bullet> bullets;
    private int health = 7;
    private List<Wall> walls;
    private int moveSpeed = 5;
    private Random random = new Random();

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final int MAX_HEALTH = 7;

    private Image upSprite, downSprite, leftSprite, rightSprite;

    private BulletFactory bulletFactory; // Добавляем поле для фабрики пуль

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    // Обновляем конструктор, чтобы принимать BulletFactory
    public PlayerTank(int x, int y, List<Wall> walls, BulletFactory bulletFactory) {
        this.x = x;
        this.y = y;
        this.direction = Direction.UP;
        this.bullets = new ArrayList<>();
        this.walls = walls;
        this.bulletFactory = bulletFactory; // Инициализируем фабрику
        loadSprites();
    }

    private void loadSprites() {
        try {
            upSprite = ImageIO.read(getClass().getResource("/tank_up.png"));
            downSprite = ImageIO.read(getClass().getResource("/tank_down.png"));
            leftSprite = ImageIO.read(getClass().getResource("/tank_left.png"));
            rightSprite = ImageIO.read(getClass().getResource("/tank_right.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Не удалось загрузить спрайты танка: " + e.getMessage());
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }

    private boolean checkCollision(int newX, int newY) {
        Rectangle playerRect = new Rectangle(newX, newY, WIDTH, HEIGHT);
        for (Wall wall : walls) {
            if (playerRect.intersects(wall.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public void moveUp() {
        int newY = y - moveSpeed;
        if (!checkCollision(x, newY)) {
            y = newY;
            direction = Direction.UP;
        }
    }

    public void moveDown() {
        int newY = y + moveSpeed;
        if (!checkCollision(x, newY)) {
            y = newY;
            direction = Direction.DOWN;
        }
    }

    public void moveLeft() {
        int newX = x - moveSpeed;
        if (!checkCollision(newX, y)) {
            x = newX;
            direction = Direction.LEFT;
        }
    }

    public void moveRight() {
        int newX = x + moveSpeed;
        if (!checkCollision(newX, y)) {
            x = newX;
            direction = Direction.RIGHT;
        }
    }

    public void shoot() {
        // Используем фабрику для создания пули
        bullets.add(bulletFactory.createBullet(x + WIDTH / 2, y + HEIGHT / 2, direction.toString()));
    }

    public void update() {
        if (isAlive()) {
            for (Bullet bullet : bullets) {
                bullet.update(800, 600);
            }
            bullets.removeIf(bullet -> !bullet.isActive());
        }
    }

    public void draw(Graphics g) {
        if (isAlive()) {
            Image sprite = getCurrentSprite();
            if (sprite != null) {
                g.drawImage(sprite, x, y, WIDTH, HEIGHT, null);
            } else {
                g.setColor(Color.GREEN);
                g.fillRect(x, y, WIDTH, HEIGHT);
            }

            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }

            drawHealthBar(g);
        }
    }

    private void drawHealthBar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y - 10, WIDTH, 5);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 10, (int) ((health / (double) MAX_HEALTH) * WIDTH), 5);
    }

    private Image getCurrentSprite() {
        switch (direction) {
            case UP:
                return upSprite;
            case DOWN:
                return downSprite;
            case LEFT:
                return leftSprite;
            case RIGHT:
                return rightSprite;
            default:
                return null;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void takeDamage(int damage) {
        if (health > 0) {
            health -= damage;
            if (health < 0) {
                health = 0;
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public void upgradeToLevel2() {
        this.moveSpeed += 1;
        this.health += 2;
    }

    public void upgradeToLevel3() {
        this.moveSpeed += 1;
        this.health += 3;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void heal(int amount) {
        if (health < MAX_HEALTH) {
            int healAmount = random.nextInt(2) + 1;
            health += healAmount;
            if (health > MAX_HEALTH) {
                health = MAX_HEALTH;
            }
        }
    }
}