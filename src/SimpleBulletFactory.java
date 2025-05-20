import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class SimpleBulletFactory implements BulletFactory {
    @Override
    public Bullet createBullet(int x, int y, String direction) {
        Image bulletImage = null;
        try {
            switch (direction) {
                case "UP" -> bulletImage = ImageIO.read(getClass().getResource("/bullet_up.png"));
                case "DOWN" -> bulletImage = ImageIO.read(getClass().getResource("/bullet_down.png"));
                case "LEFT" -> bulletImage = ImageIO.read(getClass().getResource("/bullet_left.png"));
                case "RIGHT" -> bulletImage = ImageIO.read(getClass().getResource("/bullet_right.png"));
            }
        } catch (IOException e) {
            System.err.println("Не удалось загрузить изображение пули в фабрике: " + e.getMessage());
        }
        return new Bullet(x, y, direction, bulletImage); // Передаем изображение в конструктор Bullet
    }
}
