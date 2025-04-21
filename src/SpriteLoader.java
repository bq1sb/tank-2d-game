import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class SpriteLoader {
    private static final Map<String, BufferedImage> spriteCache = new HashMap<>();

    // Метод для загрузки спрайтов с кэшированием
    public static BufferedImage load(String path) {
        // Если спрайт уже загружен, возвращаем его из кэша
        if (spriteCache.containsKey(path)) {
            return spriteCache.get(path);
        }

        // Если спрайт еще не загружен
        try {
            BufferedImage image = ImageIO.read(SpriteLoader.class.getResource(path));
            spriteCache.put(path, image);  // Кэшируем загруженный спрайт
            return image;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Не удалось загрузить спрайт: " + path);
            return getEmptyImage();  // Возвращаем пустое изображение, если загрузка не удалась
        }
    }

    // Метод для получения пустого изображения
    private static BufferedImage getEmptyImage() {
        // Возвращаем изображение 1x1 пиксель белого цвета
        BufferedImage emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        emptyImage.setRGB(0, 0, 0xFFFFFF);  // Белый цвет
        return emptyImage;
    }
}


