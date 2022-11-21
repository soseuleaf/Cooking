package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.BlockType;

public class Trash extends InteractionBlock {
    public Trash(int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize, Assets.trash, BlockType.Trash, 1);
    }

    @Override
    public void action() {

    }

    @Override
    public void update() {
        if (isHoldFood()) clearFood();
    }

    @Override
    public RenderData getFoodRenderData(int index) {
        return null;
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
    }
}
