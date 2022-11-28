package Component.Base;

import Component.*;
import Component.Type.DepthType;
import Component.Static.Config;
import Component.Render.Animation;
import Component.Static.Assets;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.DTO.StringRenderData;
import Component.Type.FoodType;
import lombok.Getter;

import java.awt.image.BufferedImage;

public abstract class Character extends Object {
    @Getter
    private final String name;
    protected Food holdFood = null;
    protected boolean isLookedRight = true;
    protected Animation animLeft = new Animation(50, Assets.CHARACTER_MOVE_LEFT);
    protected Animation animRight = new Animation(50, Assets.CHARACTER_MOVE_RIGHT);
    protected Animation animIdleLeft = new Animation(999, Assets.CHARACTER_IDLE_LEFT);
    protected Animation animIdleRight = new Animation(999, Assets.CHARACTER_IDLE_RIGHT);
    protected Animation currentAnimation = null;


    public Character(int x, int y, String name) {
        super(x, y, Config.TileSize, Config.TileSize, Assets.CHARACTER_IDLE_LEFT[0], DepthType.OBJECT);
        this.name = name;
    }

    public boolean canHold() {
        return holdFood == null;
    }

    public boolean isHold() {
        return holdFood != null;
    }

    public void addFood(Food food) {
        holdFood = food;
        holdFood.setParentPosition(x, y, true);
    }

    public Food peekFood() {
        return holdFood;
    }

    public Food popFood() {
        Food food = holdFood;
        holdFood = null;
        return food;
    }

    public FoodType getFoodType() {
        return holdFood == null ? null : holdFood.getFoodType();
    }

    public RenderData getStringRenderData() {
        return new StringRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getName());
    }

    @Override
    public RenderData getImageRenderData() {
        BufferedImage outSprite;

        if (currentAnimation == null) {
            outSprite = getSprite();
        } else {
            outSprite = currentAnimation.getCurrentFrame();
        }

        return new ImageRenderData(x, y, getWidth(), getHeight(), outSprite, getDepth());
    }
}
