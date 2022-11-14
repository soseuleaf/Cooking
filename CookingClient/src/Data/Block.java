package Data;

import java.awt.image.BufferedImage;

public class Block extends Object {
    private int boundX;
    private int boundY;
    private final boolean solid;
    protected boolean canHold;
    protected Food holdFood = null;

    public Block(int x, int y, int width, int height, BufferedImage sprite, RenderDepth depth, boolean solid, boolean hold) {
        super(x, y);
        this.solid = solid;
        this.canHold = hold;
        setSize(width, height);
        setSprite(sprite);
        setDepth(depth);
        setBound(Config.TileSize, Config.TileSize);
    }

    protected void setBound(int boundX, int boundY) {
        this.boundX = boundX;
        this.boundY = boundY;
    }

    public boolean isCanHold() {
        return canHold;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isHoldFood() {
        return holdFood != null;
    }

    public boolean addFood(Food food) {
        if (!isHoldFood()) {
            holdFood = food;
            holdFood.setParentPosition(x, y);
            return true;
        }
        return false;
    }

    public Food peekFood() {
        return holdFood;
    }

    public Food popFood() {
        Food food = holdFood;
        holdFood = null;
        return food;
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
    }
}
