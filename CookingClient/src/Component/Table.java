package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Config;
import Component.Type.BlockType;
import Component.Type.WorkState;

import java.awt.image.BufferedImage;

public class Table extends InteractionBlock {
    public Table(int x, int y, BufferedImage sprite) {
        super(x, y, Config.TileSize, Config.TileSize, sprite, BlockType.Table, 1);
    }

    @Override
    public boolean canFood(Food food) {
        return true;
    }

    @Override
    public void action() {
        System.out.println("?????");
    }

    @Override
    public void update() {
        switch (workState) {
            case NONE -> {
                if (isHoldFood()) workState = WorkState.DONE;
            }
            case DONE -> {
                if (!isHoldFood()) workState = WorkState.NONE;
            }
        }
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
