import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerTank {
    private int x, y;
    private Direction direction;
    private List<Bullet> bullets;
    private int health = 8;  // Изначально 5 жизней
    private List<Wall> walls;
    private int moveSpeed = 5; // Уменьшенная скорость

    private static final int WIDTH = 32; // Уменьшенный размер
    private static final int HEIGHT = 32; // Уменьшенный размер

    private Image upSprite, downSprite, leftSprite, rightSprite;
    private boolean alive = true; // Новый флаг для состояния танка

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public PlayerTank(int x, int y, List<Wall> walls) {
        this.x = x;
        this.y = y;
        this.direction = Direction.UP;
        this.bullets = new ArrayList<>();
        this.walls = walls;
        loadSprites();
    }

    public void reset() {
        // Сбрасываем положение танка и здоровье
        this.x = GameMap.TILE_SIZE * 2; // Начальная позиция по X
        this.y = GameMap.TILE_SIZE; // Начальная позиция по Y
        this.health = 5; // Начальное здоровье
        this.alive = true; // Восстанавливаем танк как живой
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
        bullets.add(new Bullet(x + WIDTH / 2, y + HEIGHT / 2, direction.toString()));
    }


    public void update() {
        if (isAlive()) {
            // Обновление пуль
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
                bullet.draw(g);  // Рисуем пули
            }

            drawHealthBar(g);  // Рисуем шкалу здоровья
        }
    }

    private void drawHealthBar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y - 10, WIDTH, 5);  // Фон шкалы здоровья
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 10, (int) ((health / 5.0) * WIDTH), 5);  // Заполнение шкалы здоровья
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

    // Метод для получения урона
    public void takeDamage(int damage) {
        if (health > 0) {
            health -= damage;
            if (health < 0) {
                health = 0;
            }
        }
    }

    // Метод для получения текущего здоровья
    public int getHealth() {
        return health;
    }

    public void upgradeToLevel2() {
        this.moveSpeed += 1; // Увеличиваем скорость
        this.health += 2;   // Увеличиваем здоровье
    }

    public void upgradeToLevel3() {
        this.moveSpeed += 1; // Еще больше увеличиваем скорость
        this.health += 3;   // Дополнительное здоровье
    }

    public boolean isAlive() {
        return health > 0;
    }
}
