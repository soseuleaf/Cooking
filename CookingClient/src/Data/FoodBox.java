package Data;

import java.awt.image.BufferedImage;

public class FoodBox extends InteractionBlock {
    public FoodBox(int x, int y, BufferedImage sprite) {
        super(x, y, Config.TileSize, 64, sprite, null, 1, true);
    }

    @Override
    public Food popFood() {
        return this.peekFood().clone();
    }

    @Override
    public RenderData getFoodRenderData(int index) {
        Food food = peekFood().clone();
        return new ImageRenderData(getX(), food.getY() - Config.TileSize, food.getWidth(), food.getHeight(), food.getSprite(), RenderDepth.UI);
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
    }
}
