import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Wall extends JComponent {
    protected int x, y;
    protected int health;
    private static final int WIDTH = GameMap.TILE_SIZE;
    private static final int HEIGHT = GameMap.TILE_SIZE;
    private static final Random RANDOM = new Random();

    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = 1;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
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

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isDestroyed() {
        return health <= 0;
    }
    public void draw(Graphics g) {
        g.setColor(new Color(92, 85, 85));
        g.fillRect(x, y, WIDTH, HEIGHT);
        g.setColor(new Color(35, 30, 30));
        g.drawRect(x, y, WIDTH, HEIGHT);
        drawCracks(g);
    }

    private void drawCracks(Graphics g) {
        if (RANDOM.nextFloat() < 0.2) {
            int crackX = x + RANDOM.nextInt(WIDTH);
            int crackY = y + RANDOM.nextInt(HEIGHT);
            g.drawLine(crackX, crackY, crackX + 5, crackY + 5);
        }

        if (RANDOM.nextFloat() < 0.1) {
            int crackX = x + RANDOM.nextInt(WIDTH);
            int crackY = y + RANDOM.nextInt(HEIGHT);
            g.drawLine(crackX, crackY + 3, crackX + 3, crackY - 2);
        }
    }
}