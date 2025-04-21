import javax.swing.*;
import java.awt.*;

public class Tank {
    protected int x, y;
    protected String direction;
    protected Image tankImage;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = "DOWN";
        updateTankImage();
    }

    protected void updateTankImage() {
        // логика для обновления изображения танка
        switch (direction) {
            case "UP":
                tankImage = new ImageIcon(getClass().getResource("/tank_up.png")).getImage();
                break;
            case "DOWN":
                tankImage = new ImageIcon(getClass().getResource("/tank_down.png")).getImage();
                break;
            case "LEFT":
                tankImage = new ImageIcon(getClass().getResource("/tank_left.png")).getImage();
                break;
            case "RIGHT":
                tankImage = new ImageIcon(getClass().getResource("/tank_right.png")).getImage();
                break;
        }
    }

    public Bullet fire() {
        int bulletX = x + 20;
        int bulletY = y + 20;

        // смещаем чтобы пуля вылетала из танка
        switch (direction) {
            case "UP" -> bulletY -= 20;
            case "DOWN" -> bulletY += 20;
            case "LEFT" -> bulletX -= 20;
            case "RIGHT" -> bulletX += 20;
        }

        return new Bullet(bulletX, bulletY, direction);
    }


    // Метод для рисования танка
    public void draw(Graphics g) {
        g.drawImage(tankImage, x, y, 50, 50, null); // Рисуем танк
    }

    // Методы движения танка
    public void moveUp() {
        y -= 5;
        direction = "UP";
        updateTankImage();
    }

    public void moveDown() {
        y += 5;
        direction = "DOWN";
        updateTankImage();
    }

    public void moveLeft() {
        x -= 5;
        direction = "LEFT";
        updateTankImage();
    }

    public void moveRight() {
        x += 5;
        direction = "RIGHT";
        updateTankImage();
    }

    // Геттеры и сеттеры
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
        updateTankImage();
    }
}





