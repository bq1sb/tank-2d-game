import java.awt.*;

class BrickWallData {
    private final Color brickColor = new Color(33, 30, 27);
    private final Color borderColor = new Color(115, 78, 33);
    private final Color crackColor = Color.BLACK;

    public Color getBrickColor() {
        return brickColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getCrackColor() {
        return crackColor;
    }

    public void drawCracks(Graphics g, int x, int y, int health) {
        if (health < 5) {
            g.setColor(crackColor);
            for (int i = 0; i < 5 - health; i++) {
                int crackX = x + (i * 5) + 5;
                int crackY = y + 10;
                g.drawLine(crackX, crackY, crackX + 5, crackY + 5);
            }
        }
    }
}
