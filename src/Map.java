import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Map {
    private Image mapImage;
    private final int width;
    private final int height;

    public Map() {
        this.width = 1600;  // ширина карты
        this.height = 1200; // высота карты

        try {
            mapImage = ImageIO.read(getClass().getResource("/map.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, width, height, null); // Растягиваем карту
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
        }
    }

    // Геттеры, если нужно будет прокручивать карту
    //public int getWidth() {
        //return width;


    //public int getHeight() {
        //return height;

}


