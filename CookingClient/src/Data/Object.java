package Data;

import java.awt.image.BufferedImage;

public class Object {
    private final int x;
    private final int y;
    private final int boundX = Config.TileSize;
    private final int boundY = Config.TileSize;
    protected final BufferedImage sprite;
    private final RenderDepth depth;
    private final boolean solid;

    public Object(int x, int y, BufferedImage sprite, RenderDepth depth, boolean solid) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.depth = depth;
        this.solid = solid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBoundX() {
        return boundX;
    }

    public int getBoundY() {
        return boundY;
    }

    public boolean isSolid() {
        return solid;
    }

    public RenderDepth getDepth() {
        return depth;
    }

    public BufferedImage getSprite() {
        return sprite;
    }
}
