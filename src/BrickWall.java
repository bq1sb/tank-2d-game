import java.awt.*;
import java.awt.Rectangle;


public class BrickWall extends Wall {
    private int health = 5;
    private static BrickWallData brickWallData = new BrickWallData();

    public BrickWall(int x, int y) {
        super(x, y);
    }

    public void takeDamage() {
        health--;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(super.getX(), super.getY(), GameMap.TILE_SIZE, GameMap.TILE_SIZE);
    }

    @Override
    public void draw(Graphics g) {
        if (!isDestroyed()) {
            g.setColor(brickWallData.getBrickColor());
            g.fillRect(getX(), getY(), GameMap.TILE_SIZE, GameMap.TILE_SIZE);
            g.setColor(brickWallData.getBorderColor());
            g.drawRect(getX(), getY(), GameMap.TILE_SIZE, GameMap.TILE_SIZE);

            brickWallData.drawCracks(g, getX(), getY(), health);
        }
    }
}