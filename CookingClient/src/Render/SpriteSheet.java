package Render;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage sheet;
    private final BufferedImage sheetOriginal;

    public SpriteSheet(BufferedImage sheet) {
        this.sheet = copyImage(sheet);
        this.sheetOriginal = copyImage(sheet);
    }

    public SpriteSheet crop(int x, int y, int width, int height) {
        this.sheet = sheet.getSubimage(x, y, width, height);
        return this;
    }

    public SpriteSheet flipX() {
        AffineTransform at = new AffineTransform();
        at.scale(-1.0, 1.0);
        at.translate(-sheet.getWidth(), 0);
        this.sheet = createTransformed(sheet, at);
        return this;
    }

    public BufferedImage build() {
        BufferedImage tmp = copyImage(sheet);
        this.sheet = copyImage(sheetOriginal);
        return tmp;
    }

    public BufferedImage copyImage(BufferedImage image) {
        BufferedImage b = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = b.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return b;
    }

    private BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}