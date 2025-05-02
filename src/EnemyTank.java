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
    private List<Wall> walls;

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final double DETECTION_RADIUS = 300.0;
    private static final int SHOOTING_COOLDOWN = 500;
    private static final double MIN_DISTANCE_FROM_PLAYER = 70.0;

    private int screenWidth;
    private int screenHeight;

    private long lastShootTime = 0;
    private Image upSprite, downSprite, leftSprite, rightSprite;

    public EnemyTank(PlayerTank playerTank, List<Wall> walls) {
        this.playerTank = playerTank;
        this.walls = walls;
        loadSprites();
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

    public void reset(int fieldWidth, int fieldHeight) {
        this.screenWidth = fieldWidth;
        this.screenHeight = fieldHeight;

        int randSide = (int) (Math.random() * 4);
        switch (randSide) {
            case 0 -> { this.x = 0; this.y = (int) (Math.random() * screenHeight); }
            case 1 -> { this.x = screenWidth - WIDTH; this.y = (int) (Math.random() * screenHeight); }
            case 2 -> { this.x = (int) (Math.random() * screenWidth); this.y = screenHeight - HEIGHT; }
            case 3 -> { this.x = (int) (Math.random() * screenWidth); this.y = 0; }
        }

        this.health = 5;
        this.direction = (int) (Math.random() * 4);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void update(int fieldWidth, int fieldHeight) {
        this.screenWidth = fieldWidth;
        this.screenHeight = fieldHeight;

        if (isPlayerInRange()) {
            if (!isTooCloseToPlayer()) {
                moveToPlayer();
            } else {
                moveRandomly();
            }

            if (canShoot() && isPlayerInDirectSight()) {
                shoot();
            }
        } else {
            moveRandomly();
        }

        for (Bullet bullet : bullets) {
            bullet.update(fieldWidth, fieldHeight);
        }

        bullets.removeIf(bullet -> !bullet.isActive());
    }

    private boolean isPlayerInRange() {
        int dx = playerTank.getX() - x;
        int dy = playerTank.getY() - y;
        return Math.sqrt(dx * dx + dy * dy) <= DETECTION_RADIUS;
    }

    private boolean isTooCloseToPlayer() {
        int dx = playerTank.getX() - x;
        int dy = playerTank.getY() - y;
        return Math.sqrt(dx * dx + dy * dy) <= MIN_DISTANCE_FROM_PLAYER;
    }

    // Проверка, есть ли прямой путь к игроку (без препятствий)
    private boolean isPlayerInDirectSight() {
        int dx = playerTank.getX() - x;
        int dy = playerTank.getY() - y;
        double angle = Math.atan2(dy, dx);

        // Расстояние между врагом и игроком
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Проверяем, нет ли препятствий по пути
        for (double i = 0; i < distance; i += 10) {
            int checkX = x + (int) (i * Math.cos(angle));
            int checkY = y + (int) (i * Math.sin(angle));

            if (checkCollision(checkX, checkY)) {
                return false; // Препятствия на пути
            }
        }
        return true; // Прямой путь к игроку
    }

    private boolean checkCollision(int newX, int newY) {
        Rectangle newBounds = new Rectangle(newX, newY, WIDTH, HEIGHT);
        for (Wall wall : walls) {
            if (newBounds.intersects(wall.getBounds())) return true;
        }
        return false;
    }

    private void moveToPlayer() {
        int nextX = x, nextY = y;
        if (playerTank.getX() > x && x + speed < screenWidth - WIDTH) nextX = x + speed;
        else if (playerTank.getX() < x && x - speed >= 0) nextX = x - speed;

        if (playerTank.getY() > y && y + speed < screenHeight - HEIGHT) nextY = y + speed;
        else if (playerTank.getY() < y && y - speed >= 0) nextY = y - speed;

        if (!checkCollision(nextX, nextY)) {
            x = nextX;
            y = nextY;
        }

        updateDirection();
    }

    private void moveRandomly() {
        if (Math.random() < 0.01) direction = (int) (Math.random() * 4);

        int nextX = x, nextY = y;
        switch (direction) {
            case 0 -> nextY = y - speed;
            case 1 -> nextY = y + speed;
            case 2 -> nextX = x - speed;
            case 3 -> nextX = x + speed;
        }

        if (!checkCollision(nextX, nextY)) {
            x = nextX;
            y = nextY;
        }
    }

    private void updateDirection() {
        int dx = playerTank.getX() - x;
        int dy = playerTank.getY() - y;

        if (Math.abs(dx) > Math.abs(dy)) direction = dx > 0 ? 3 : 2;
        else direction = dy > 0 ? 1 : 0;
    }

    private boolean canShoot() {
        long now = System.currentTimeMillis();
        if (now - lastShootTime >= SHOOTING_COOLDOWN) {
            lastShootTime = now;
            return true;
        }
        return false;
    }

    private void shoot() {
        if (Math.random() < 0.4) bullets.add(createBullet());
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
        g.fillRect(x, y - 10, (int) ((health / 5.0) * WIDTH), 5);
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

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage() {
        health--;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
