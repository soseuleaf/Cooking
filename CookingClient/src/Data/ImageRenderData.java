package Data;

import java.awt.image.BufferedImage;

public class ImageRenderData extends RenderData {
    private final BufferedImage sprite;

    public ImageRenderData(int x, int y, BufferedImage sprite, RenderDepth z) {
        super(x, y, z);
        this.sprite = sprite;
    }

    public BufferedImage getSprite() {
        return sprite;
    }
}
