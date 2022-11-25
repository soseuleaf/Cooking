package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.BlockType;

public class FoodOut extends InteractionBlock {
    public FoodOut(int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize, Assets.foodout, BlockType.FoodOut, 1);
    }

    @Override
    public boolean canFood(Food food) {
        return true;
    }

    @Override
    public boolean canPop() {
        return false;
    }

    @Override
    public boolean canAction() {
        return false;
    }

    @Override
    public void action() {
    }

    @Override
    public void update() {
        clearFood();
    }

    @Override
    public RenderData getFoodRenderData(int index) {
        return peekFood().getImageRenderData();
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
    }
}
