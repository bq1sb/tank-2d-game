import java.awt.*;

public class BreakableWall {
    private int x, y;
    private int health = 5; // Прочность стены (5 попаданий)
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    public BreakableWall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (health > 0) {
            g.setColor(new Color(139, 69, 19)); // Коричневый цвет кирпича
            g.fillRect(x, y, WIDTH, HEIGHT);
            // Можно добавить отрисовку трещин в зависимости от уровня здоровья
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void takeDamage() {
        if (health > 0) {
            health--;
        }
    }

    public boolean isBroken() {
        return health <= 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }
}

