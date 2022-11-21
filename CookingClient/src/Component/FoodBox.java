package Component;

import Component.Static.Assets;
import Component.Type.BlockType;
import Component.Type.DepthType;
import Component.Base.InteractionBlock;
import Component.Static.Config;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Type.WorkState;

import java.awt.image.BufferedImage;

public class FoodBox extends InteractionBlock {
    private final Food tempFood;
    private final BufferedImage[] refrig = Assets.refrig;
    private BufferedImage currentSprite;

    public FoodBox(int x, int y, Food food) {
        super(x, y, Config.TileSize, Config.TileSize * 2, Assets.refrig[0], BlockType.FoodBox, null, 1);
        this.tempFood = food.clone();
        this.workState = WorkState.DONE;
        this.currentSprite = refrig[0];
        addFood(food);
    }

    @Override
    public boolean isCanAdd() {
        return false;
    }

    @Override
    public void action() {
        addProgress(50);
    }

    @Override
    public void update() {
        switch (workState) {
            case WORKING -> progressValue++;
            case DONE -> progressValue = 0;
        }
        if (!isHoldFood()) {
            workState = WorkState.WORKING;
        }
        if (progressValue > progressMax) {
            addFood(tempFood.clone());
            workState = WorkState.DONE;
        }

        if (progressValue < 50 && progressValue > 0) {
            currentSprite = refrig[1];
        } else {
            currentSprite = refrig[0];
        }
    }

    @Override
    public RenderData getFoodRenderData(int index) {
        Food food = peekFood();
        return new ImageRenderData(getX(), food.getY() - Config.TileSize, food.getWidth(), food.getHeight(), food.getSprite(), DepthType.UI);
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), currentSprite, getDepth());
    }
}
