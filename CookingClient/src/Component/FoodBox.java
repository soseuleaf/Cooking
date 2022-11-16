package Component;

import Component.Type.DepthType;
import Component.Base.InteractionBlock;
import Component.Static.Config;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;

import java.awt.image.BufferedImage;

public class FoodBox extends InteractionBlock {
    public FoodBox(int x, int y, BufferedImage sprite) {
        super(x, y, Config.TileSize, 64, sprite, null, 1, true);
    }

    public Food cloneFood() {
        return this.peekFood().clone();
    }

    @Override
    public RenderData getFoodRenderData(int index) {
        Food food = peekFood().clone();
        return new ImageRenderData(getX(), food.getY() - Config.TileSize, food.getWidth(), food.getHeight(), food.getSprite(), DepthType.UI);
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
    }
}
