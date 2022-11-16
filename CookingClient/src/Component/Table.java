package Component;

import Component.Base.InteractionBlock;
import Component.Static.Config;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;

import java.awt.image.BufferedImage;

public class Table extends InteractionBlock {
    public Table(int x, int y, BufferedImage sprite) {
        super(x, y, Config.TileSize, Config.TileSize, sprite, null, 1, true);
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
