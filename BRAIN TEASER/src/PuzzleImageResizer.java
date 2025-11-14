import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class PuzzleImageResizer {
    public static BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaled = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
