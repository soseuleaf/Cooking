package Component;

import Component.Type.DepthType;
import Component.Base.Object;
import Component.Static.Config;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
public class Block extends Object {
    private final int boundX;
    private final int boundY;
    private final boolean solid;

    public Block(int x, int y, int width, int height, BufferedImage sprite, DepthType depth, boolean solid) {
        super(x, y, width, height, sprite, depth);
        this.solid = solid;
        this.boundX = Config.TileSize;
        this.boundY = Config.TileSize;
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
    }
}
