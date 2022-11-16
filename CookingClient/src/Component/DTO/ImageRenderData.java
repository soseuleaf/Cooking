package Component.DTO;

import Component.Type.DepthType;

import java.awt.image.BufferedImage;

public class ImageRenderData extends RenderData {
    private final int width;
    private final int height;
    private final BufferedImage sprite;

    public ImageRenderData(int x, int y, int width, int height, BufferedImage sprite, DepthType z) {
        super(x, y, z);
        this.sprite = sprite;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getSprite() {
        return sprite;
    }
}
