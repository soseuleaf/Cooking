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

public class Pot extends InteractionBlock {
    private final BufferedImage[] pot = Assets.pot;
    private BufferedImage currentSprite;

    public Pot(int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize * 2, Assets.pot[0], BlockType.Pot, null, 4);
        currentSprite = Assets.pot[0];
    }

    @Override
    public void action() {
        addProgress(1);
    }

    @Override
    public void update() {
        if (progressValue > progressMax) {
            progressValue = 0;
            Food temp = holdFood[0];
            for (int i = 1; i < getHoldFoodMax(); i++) {
                if (holdFood[i].getFoodType() != temp.getFoodType()) {
                    clearFood();
                    holdFoodMax = 1;
                    addFood(Assets.FOODLIST.get(0).clone());
                    workState = WorkState.DONE;
                    return;
                }
            }
            clearFood();
            holdFoodMax = 1;
            addFood(Assets.FOODLIST.get(1).clone());
            workState = WorkState.DONE;
        } else if (workState == WorkState.WORKING) {
            if (progressValue < 50 || progressValue > 53) progressValue += 1;
        } else if (workState == WorkState.NONE && getHoldFoodIndex() == getHoldFoodMax()) {
            workState = WorkState.WORKING;
            currentSprite = pot[1];
        } else if (workState == WorkState.DONE && !isHoldFood()) {
            workState = WorkState.NONE;
            holdFoodMax = 4;
            currentSprite = pot[0];
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
