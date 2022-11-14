package Data;

import Render.Assets;

import java.awt.image.BufferedImage;

public class Food {
    private int id = 1;
    private int size = Config.TileSize;
    private RenderDepth depth = RenderDepth.OBJECT;
    boolean isOriginal = true;
    boolean isFire = false;
    boolean isKnife = false;
    BufferedImage sprite = Assets.TEST;

    public Food(){
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public RenderDepth getDepth() {
        return depth;
    }
}
