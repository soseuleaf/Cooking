package Component;

import Component.Base.InteractionBlock;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.WorkState;

import java.awt.image.BufferedImage;

public class Knife extends InteractionBlock {
    public Knife(int x, int y, BufferedImage sprite) {
        super(x, y, Config.TileSize, Config.TileSize, sprite, null, 1);
    }

    public Food getSlicedFood() {
        Food food = popFood();
        workState = WorkState.NONE;
        return food;
    }

    @Override
    public void action() {
        addProgress(55);
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
        } else if (workState == WorkState.NONE && isHoldFood()) {
            workState = WorkState.WORKING;
            System.out.println(getProgressValue());
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
