import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class SpriteLoader {
    private static final Map<String, BufferedImage> spriteCache = new HashMap<>();
    public static BufferedImage load(String path) {
        if (spriteCache.containsKey(path)) {
            return spriteCache.get(path);
        }

        try {
            BufferedImage image = ImageIO.read(SpriteLoader.class.getResource(path));
            spriteCache.put(path, image);
            return image;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Не удалось загрузить спрайт: " + path);
            return getEmptyImage();
        }
    }
    private static BufferedImage getEmptyImage() {
        BufferedImage emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        emptyImage.setRGB(0, 0, 0xFFFFFF);
        return emptyImage;
    }
}


