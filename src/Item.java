import java.awt.*;
import java.awt.Rectangle;

public class Item {
    private int x;
    private int y;
    private int size = 8; // Размер точки-зелья
    private Color color = Color.RED;
    private boolean collected = false;

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (!collected) {
            g.setColor(color);
            g.fillRect(x, y, size, size);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public void applyEffect(PlayerTank playerTank) {
        playerTank.heal(2); // Лечим танк на 1 единицу
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
