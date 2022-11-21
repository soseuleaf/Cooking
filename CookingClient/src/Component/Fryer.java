package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.BlockType;
import Component.Type.DepthType;
import Component.Type.WorkState;

import java.awt.image.BufferedImage;

public class Fryer extends InteractionBlock {
    private final BufferedImage[] fryer = new BufferedImage[2];
    private BufferedImage currentSprite;

    public Fryer(int x, int y, boolean isLeft) {
        super(x, y, Config.TileSize, Config.TileSize * 2, Assets.fryer[0], BlockType.Fryer, 1);
        if (isLeft) {
            fryer[0] = Assets.fryer[0];
            fryer[1] = Assets.fryer[2];
        } else {
            fryer[0] = Assets.fryer[1];
            fryer[1] = Assets.fryer[3];
        }
        currentSprite = fryer[0];
    }

    @Override
    public void action() {
    }

    @Override
    public void update() {
        if (progressValue > progressMax) {
            progressValue = 0;
            switch (popFood().getFoodType()) {
                case TEST -> addFood(Assets.FOODLIST.get(1).clone());
                case APPLE -> addFood(Assets.FOODLIST.get(2).clone());
                case MEAT -> addFood(Assets.FOODLIST.get(0).clone());
                default -> addFood(Assets.FOODLIST.get(0).clone());
            }
            workState = WorkState.DONE;
        } else if (workState == WorkState.WORKING) {
            progressValue += 0.25;
        } else if (workState == WorkState.NONE && isHoldFood()) {
            workState = WorkState.WORKING;
            currentSprite = fryer[1];
        } else if (workState == WorkState.DONE && !isHoldFood()) {
            workState = WorkState.NONE;
            currentSprite = fryer[0];
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
