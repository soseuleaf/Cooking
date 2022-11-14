package Data;

import Render.Assets;

import java.awt.image.BufferedImage;

public class Food extends Object {
    private int id = 1;
    boolean isOriginal = true;
    boolean isFire = false;
    boolean isKnife = false;
    BufferedImage sprite;

    public Food() {
        super(0, 0);
        setSprite(Assets.TEST);
    }

    public int getId() {
        return id;
    }

    public void setParentPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public RenderData getStringRenderData() {
        return new StringRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), Integer.toString(id));
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
    }
}
