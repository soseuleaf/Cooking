package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Static.SoundPlayer;
import Component.Type.*;

import java.awt.image.BufferedImage;

public class Pot extends InteractionBlock {
    private final BufferedImage[] pot = Assets.pot;
    private BufferedImage currentSprite;

    public Pot(int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize * 2, Assets.pot[0], BlockType.Pot, 4);
        currentSprite = Assets.pot[0];
    }

    @Override
    public void playSoundEffect() {
        SoundPlayer.play(SoundType.POT);
    }

    @Override
    public boolean canFood(Food food) {
        if (!isHoldFood()) {
            return switch (food.getFoodType()) {
                case ONION, TOMATO, MUSHROOM -> true;
                default -> false;
            };
        } else {
            return peekFood().getFoodType() == food.getFoodType();
        }
    }

    @Override
    public void action() {
        workState = WorkState.WORKING;
    }

    @Override
    public void update() {
        switch (workState) {
            case NONE -> {
                if (getHoldFoodIndex() == getHoldFoodMax()) {
                    workState = WorkState.WORKING;
                    currentSprite = pot[1];
                }
            }
            case WORKING -> {
                progressValue += 0.25;
                if (progressValue > progressMax) {
                    progressValue = 0;
                    Food temp = new Food(FoodType.EGG);
                    switch (peekFood().getFoodType()) {
                        case ONION -> temp = Assets.FOODLIST.get(FoodType.ONION_SOUP.ordinal()).clone();
                        case TOMATO -> temp = Assets.FOODLIST.get(FoodType.TOMATO_SOUP.ordinal()).clone();
                        case MUSHROOM -> temp = Assets.FOODLIST.get(FoodType.MUSHROOM_SOUP.ordinal()).clone();
                    }
                    clearFood();
                    addFood(temp.clone());
                    addFood(temp.clone());
                    addFood(temp.clone());
                    addFood(temp.clone());
                    workState = WorkState.DONE;
                } else if (progressValue % 25 == 24) {
                    workState = WorkState.NEEDACTION;
                }
            }
            case DONE -> {
                if (!isHoldFood()) {
                    currentSprite = pot[0];
                    workState = WorkState.NONE;
                }
            }
        }
    }

    @Override
    public RenderData getFoodRenderData(int index) {
        if (getHoldFoodMax() == 1) {
            Food temp = peekFood();
            temp.setParentPosition(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), false);
            return temp.getImageRenderData();
        }

        ImageRenderData temp = null;
        switch (index) {
            case 0 ->
                    temp = new ImageRenderData(x, y - Config.TileSize, Config.TileSize / 2, Config.TileSize / 2, holdFood[0].getSprite(), DepthType.EFFECT);
            case 1 ->
                    temp = new ImageRenderData(x + Config.TileSize / 2, y - Config.TileSize, Config.TileSize / 2, Config.TileSize / 2, holdFood[1].getSprite(), DepthType.EFFECT);
            case 2 ->
                    temp = new ImageRenderData(x, y - Config.TileSize + Config.TileSize / 2, Config.TileSize / 2, Config.TileSize / 2, holdFood[2].getSprite(), DepthType.EFFECT);
            case 3 ->
                    temp = new ImageRenderData(x + Config.TileSize / 2, y - Config.TileSize + Config.TileSize / 2, Config.TileSize / 2, Config.TileSize / 2, holdFood[3].getSprite(), DepthType.EFFECT);
        }
        return temp;
    }

    @Override
    public RenderData getImageRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), currentSprite, getDepth());
    }
}
