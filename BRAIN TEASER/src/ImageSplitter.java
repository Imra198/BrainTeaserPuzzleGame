import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageSplitter {
    public static List<BufferedImage> split(BufferedImage image, int level) {
        int width = image.getWidth() / level;
        int height = image.getHeight() / level;
        List<BufferedImage> images = new ArrayList<>();

        for (int row = 0; row < level; row++)
            for (int col = 0; col < level; col++)
                images.add(image.getSubimage(col * width, row * height, width, height));

        return images;
    }
}
