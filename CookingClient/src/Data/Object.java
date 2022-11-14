package Data;

import java.awt.image.BufferedImage;

public class Object {
    private final int x;
    private final int y;
    private final int boundX = Config.TileSize;
    private final int boundY = Config.TileSize;
    private int width = Config.TileSize;
    private int height = Config.TileSize;
    protected final BufferedImage sprite;
    private final RenderDepth depth;
    private final boolean solid;

    protected boolean canPut = true;
    protected Food containFood = null;

    public Object(int x, int y, BufferedImage sprite, RenderDepth depth, boolean solid) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.depth = depth;
        this.solid = solid;
    }

    protected void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setContainFood(){
        containFood = new Food();
    }

    public Food getFood(){
        return containFood;
    }

    public int getX() {
        return x + Config.TileSize - width;
    }

    public int getY() {
        return y + Config.TileSize - height;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
