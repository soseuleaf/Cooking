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
    protected Animation animLeft;
    protected Animation animRight;
    protected Animation animIdleLeft;
    protected Animation animIdleRight;
    protected Animation currentAnimation = null;


    public Character(int num, String name, int x, int y) {
        super(x, y, Config.TileSize, Config.TileSize, Assets.BLACKTILE, DepthType.OBJECT);

        switch (num) {
            default -> {
                animLeft = new Animation(50, Assets.PLAYER1_MOVE_LEFT);
                animRight = new Animation(50, Assets.PLAYER1_MOVE_RIGHT);
                animIdleLeft = new Animation(999, Assets.PLAYER1_IDLE_LEFT);
                animIdleRight = new Animation(999, Assets.PLAYER1_IDLE_RIGHT);
            }
            case 1 -> {
                animLeft = new Animation(50, Assets.PLAYER2_MOVE_LEFT);
                animRight = new Animation(50, Assets.PLAYER2_MOVE_RIGHT);
                animIdleLeft = new Animation(999, Assets.PLAYER2_IDLE_LEFT);
                animIdleRight = new Animation(999, Assets.PLAYER2_IDLE_RIGHT);
            }
            case 2 -> {
                animLeft = new Animation(50, Assets.PLAYER3_MOVE_LEFT);
                animRight = new Animation(50, Assets.PLAYER3_MOVE_RIGHT);
                animIdleLeft = new Animation(999, Assets.PLAYER3_IDLE_LEFT);
                animIdleRight = new Animation(999, Assets.PLAYER3_IDLE_RIGHT);
            }
            case 3 -> {
                animLeft = new Animation(50, Assets.PLAYER4_MOVE_LEFT);
                animRight = new Animation(50, Assets.PLAYER4_MOVE_RIGHT);
                animIdleLeft = new Animation(999, Assets.PLAYER4_IDLE_LEFT);
                animIdleRight = new Animation(999, Assets.PLAYER4_IDLE_RIGHT);
            }
        }

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
