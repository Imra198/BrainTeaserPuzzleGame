import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PuzzlePiece {
    private final int id;
    private BufferedImage image;
    private int direction = 0;
    private final int previousRow, previousCol;

    public PuzzlePiece(int id, BufferedImage image, int previousRow, int previousCol) {
        this.id = id;
        this.image = image;
        this.previousRow = previousRow;
        this.previousCol = previousCol;
    }

    public int getId() { return id; }
    public BufferedImage getImage() { return image; }
    public int getDirection() { return direction; }
    public int getpreviousRow() { return previousRow; }
    public int getpreviousCol() { return previousCol; }

    public void rotate() {
        direction = (direction + 90) % 360;
        image = imageRotate(image, direction);
    }

    private BufferedImage imageRotate(BufferedImage img, int angle) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage rotated = new BufferedImage(w, h, img.getType());
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), w / 2, h / 2);
        g2d.setTransform(transform);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return rotated;
    }
}
