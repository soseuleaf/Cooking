package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.BlockType;
import Component.Type.WorkState;

public class Knife extends InteractionBlock {
    public Knife(int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize, Assets.knife, BlockType.Knife, 1);
    }

    @Override
    public void action() {
        addProgress(55);
    }

    @Override
    public void update() {
        switch (workState) {
            case NONE -> {
                if (isHoldFood()) {
                    workState = WorkState.WORKING;
                }
            }
            case WORKING -> {
                if (progressValue > progressMax) {
                    progressValue = 0;
                    switch (popFood().getFoodType()) {
                        case TEST -> addFood(Assets.FOODLIST.get(1).clone());
                        case APPLE -> addFood(Assets.FOODLIST.get(2).clone());
                        case MEAT -> addFood(Assets.FOODLIST.get(0).clone());
                        default -> addFood(Assets.FOODLIST.get(0).clone());
                    }
                    workState = WorkState.DONE;
                }
            }
            case DONE -> {
                if (!isHoldFood()) {
                    workState = WorkState.NONE;
                }
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
