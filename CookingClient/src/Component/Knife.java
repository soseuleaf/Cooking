package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.BlockType;
import Component.Type.FoodType;
import Component.Type.WorkState;

public class Knife extends InteractionBlock {
    public Knife(int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize, Assets.knife, BlockType.Knife, 1);
    }

    @Override
    public boolean canFood(Food food) {
        return switch (food.getFoodType()) {
            case POTATO, BREAD, COOKED_MEAT -> true;
            default -> false;
        };
    }

    @Override
    public void action() {
        addProgress(7);
    }

    @Override
    public void update() {
        switch (workState) {
            case NONE -> {
                if (isHoldFood()) {
                    workState = WorkState.NEEDACTION;
                }
            }
            case NEEDACTION -> {
                if (progressValue > progressMax) {
                    progressValue = 0;

                    switch (popFood().getFoodType()) {
                        case POTATO -> addFood(Assets.FOODLIST.get(FoodType.SLICED_POTATO.ordinal()).clone());
                        case BREAD -> addFood(Assets.FOODLIST.get(FoodType.SLICED_BREAD.ordinal()).clone());
                        case COOKED_MEAT -> addFood(Assets.FOODLIST.get(FoodType.STEAK.ordinal()).clone());
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
