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

public class Frypan extends InteractionBlock {
    private BufferedImage[] frypan;
    private BufferedImage currentSprite;

    public Frypan(int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize * 2, Assets.fryer[0], BlockType.Fryer, 1);
        frypan = Assets.frypan;
        currentSprite = frypan[0];
    }

    @Override
    public void action() {
        progressValue++;
    }

    @Override
    public void update() {
        switch (workState) {
            case NONE -> {
                if (isHoldFood()) {
                    workState = WorkState.WORKING;
                    currentSprite = frypan[1];
                }
            }
            case WORKING -> {
                if (progressValue % 10 != 0) progressValue += 0.5;
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
                    currentSprite = frypan[0];
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
