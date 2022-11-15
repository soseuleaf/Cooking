package Data;

import Render.Animation;
import Render.Assets;
import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
public abstract class InteractionBlock extends Block {
    private final boolean canPop;
    private final BufferedImage touchSprite = Assets.TILEMAP[20];
    private final Animation useAnimation;
    private final Food[] holdFood = new Food[4];
    private boolean isTouch = false;
    private final boolean isUsing = false;
    private final int holdFoodMax;
    private int holdFoodIndex = 0;

    public InteractionBlock(int x, int y, int width, int height, BufferedImage sprite, Animation animation, int holdFoodMax, boolean canPop) {
        super(x, y, width, height, sprite, RenderDepth.BIGOBJ, true);
        this.useAnimation = animation;
        this.holdFoodMax = holdFoodMax;
        this.canPop = canPop;
    }

    public void setTouch(boolean touch) {
        this.isTouch = touch;
    }

    public void addFood(Food food) {
        if (holdFoodIndex < holdFoodMax) {
            food.setParentPosition(x, y, false);
            holdFood[holdFoodIndex] = food;
            holdFoodIndex++;
        }
    }

    public Food peekFood() {
        return holdFood[holdFoodIndex - 1];
    }

    public Food popFood() {
        Food food = holdFood[holdFoodIndex - 1];
        holdFood[holdFoodIndex - 1] = null;
        holdFoodIndex--;
        return food;
    }

    public boolean isCanPop() {
        return !isUsing && canPop && isHoldFood();
    }

    public boolean isCanAdd() {
        return !isUsing && (holdFoodIndex < holdFoodMax);
    }

    public boolean isHoldFood() {
        return holdFoodIndex > 0;
    }

    public RenderData getTouchRenderData() {
        setTouch(false);
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), touchSprite, RenderDepth.EFFECT);
    }

    public abstract RenderData getFoodRenderData(int index);

    public abstract RenderData getImageRenderData();


//        BufferedImage outSprite = Assets.TILEMAP[20];
//        setTouch(false);
//        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), outSprite, RenderDepth.EFFECT);
//
//    @Override
//    public RenderData getImageRenderData() {
//        BufferedImage outSprite;
//        if (!canUse) {
//            outSprite = interaction.getCurrentFrame();
//        } else {
//            outSprite = getSprite();
//        }
//        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), outSprite, RenderDepth.OBJECT);
//    }
}
