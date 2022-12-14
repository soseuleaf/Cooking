package Component;

import Component.Static.Assets;
import Component.Static.SoundPlayer;
import Component.Type.BlockType;
import Component.Type.DepthType;
import Component.Base.InteractionBlock;
import Component.Static.Config;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Type.SoundType;
import Component.Type.WorkState;

import java.awt.image.BufferedImage;

public class FoodBox extends InteractionBlock {
    private final Food tempFood;
    private final BufferedImage[] refrig = Assets.refrig;
    private BufferedImage currentSprite;

    public FoodBox(int x, int y, Food food) {
        super(x, y, Config.TileSize, Config.TileSize * 2, Assets.refrig[0], BlockType.FoodBox, 1);
        this.tempFood = food.clone();
        this.workState = WorkState.DONE;
        this.currentSprite = refrig[0];
        addFood(food);
    }

    @Override
    public boolean canAdd() {
        return false;
    }

    @Override
    public void playSoundEffect() {
        SoundPlayer.play(SoundType.FOODBOX);
    }

    @Override
    public boolean canFood(Food food) {
        return false;
    }

    @Override
    public void action() {
    }

    @Override
    public void update() {
        switch (workState) {
            case WORKING -> {
                progressValue++;
                if (progressValue > progressMax) {
                    progressValue = 0;
                    addFood(tempFood.clone());
                    workState = WorkState.DONE;
                }

                if (progressValue < 50 && progressValue > 0) {
                    currentSprite = refrig[1];
                } else {
                    currentSprite = refrig[0];
                }
            }
            case DONE -> {
                if (!isHoldFood()) {
                    workState = WorkState.WORKING;
                }
            }
        }
    }

    @Override
    public RenderData getFoodRenderData(int index) {
        Food food = peekFood();
        return new ImageRenderData(getX(), food.getY() - Config.TileSize, food.getWidth(), food.getHeight(), food.getSprite(), DepthType.EFFECT);
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), currentSprite, getDepth());
    }
}
