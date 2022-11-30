package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Static.SoundPlayer;
import Component.Type.*;

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
    public void playSoundEffect() {
        SoundPlayer.play(SoundType.FRYER);
    }

    @Override
    public boolean canFood(Food food) {
        return switch (food.getFoodType()) {
            case SLICED_POTATO, MANDOO, CHICKEN -> true;
            default -> false;
        };
    }

    @Override
    public void action() {
        workState = WorkState.WORKING;
    }

    @Override
    public void update() {
        switch (workState) {
            case NONE -> {
                if (isHoldFood()) {
                    currentSprite = fryer[1];
                    workState = WorkState.WORKING;
                }
            }
            case WORKING -> {
                progressValue += 0.5;
                if (progressValue > progressMax) {
                    progressValue = 0;
                    switch (popFood().getFoodType()) {
                        case SLICED_POTATO -> addFood(Assets.FOODLIST.get(FoodType.FRENCH_FRIES.ordinal()).clone());
                        case MANDOO -> addFood(Assets.FOODLIST.get(FoodType.FRIED_MANDOO.ordinal()).clone());
                        case CHICKEN -> addFood(Assets.FOODLIST.get(FoodType.FRIED_CHICKEN.ordinal()).clone());
                    }
                    workState = WorkState.DONE;
                } else if (progressValue % 26 == 0) {
                    workState = WorkState.NEEDACTION;
                }
            }
            case DONE -> {
                if (!isHoldFood()) {
                    currentSprite = fryer[0];
                    workState = WorkState.NONE;
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
