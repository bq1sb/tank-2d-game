import java.awt.*;
import java.awt.Rectangle;

public class Item {
    private int x;
    private int y;
    private int size = 8;
    private Color color = Color.RED;
    private boolean collected = false;
    private ItemEffectCommand effectCommand;

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
        this.effectCommand = new HealCommand(2);
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
        if (effectCommand != null) {
            effectCommand.execute(playerTank);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void setEffectCommand(ItemEffectCommand effectCommand) {
        this.effectCommand = effectCommand;
    }
}