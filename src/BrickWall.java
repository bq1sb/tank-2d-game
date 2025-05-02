import java.awt.*;

import java.awt.Rectangle;

public class BrickWall extends Wall {
    private int health = 5; // Начальное здоровье - 5 ударов

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
            g.setColor(new Color(33, 30, 27)); // Коричневый цвет для кирпича
            g.fillRect(getX(), getY(), GameMap.TILE_SIZE, GameMap.TILE_SIZE);
            g.setColor(new Color(115, 78, 33)); // Более темный коричневый для контура
            g.drawRect(getX(), getY(), GameMap.TILE_SIZE, GameMap.TILE_SIZE);

            // Можно добавить визуальную индикацию здоровья, например, трещины
            if (health < 5) {
                g.setColor(Color.BLACK);
                for (int i = 0; i < 5 - health; i++) {
                    int crackX = getX() + (i * 5) + 5;
                    int crackY = getY() + 10;
                    g.drawLine(crackX, crackY, crackX + 5, crackY + 5);
                }
            }
        }
    }
}