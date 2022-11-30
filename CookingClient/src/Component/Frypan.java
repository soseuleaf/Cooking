package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Static.SoundPlayer;
import Component.Type.*;

import java.awt.image.BufferedImage;

public class Frypan extends InteractionBlock {
    private final BufferedImage[] frypan;
    private BufferedImage currentSprite;

    public Frypan(int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize * 2, Assets.fryer[0], BlockType.Fryerpan, 1);
        frypan = Assets.frypan;
        currentSprite = frypan[0];
    }

    @Override
    public void playSoundEffect() {
        SoundPlayer.play(SoundType.FRYPAN);
    }

    @Override
    public boolean canFood(Food food) {
        return switch (food.getFoodType()) {
            case SALMON, BACON, MEAT, EGG -> true;
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
                    workState = WorkState.WORKING;
                    currentSprite = frypan[1];
                }
            }
            case WORKING -> {
                progressValue += 0.5;
                if (progressValue > progressMax) {
                    progressValue = 0;
                    switch (popFood().getFoodType()) {
                        case SALMON -> addFood(Assets.FOODLIST.get(FoodType.COOKED_SALMON.ordinal()).clone());
                        case BACON -> addFood(Assets.FOODLIST.get(FoodType.COOKED_BACON.ordinal()).clone());
                        case MEAT -> addFood(Assets.FOODLIST.get(FoodType.COOKED_MEAT.ordinal()).clone());
                        case EGG -> addFood(Assets.FOODLIST.get(FoodType.FRIED_EGG.ordinal()).clone());
                    }
                    workState = WorkState.DONE;
                } else if (progressValue % 15 == 14) {
                    workState = WorkState.NEEDACTION;
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
