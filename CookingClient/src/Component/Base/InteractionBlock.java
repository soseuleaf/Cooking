package Component.Base;

import Component.Block;
import Component.Type.DepthType;
import Component.Food;
import Component.Static.Config;
import Component.Render.Animation;
import Component.Static.Assets;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Type.WorkState;
import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
public abstract class InteractionBlock extends Block {
    // working 상태가 되었을 때 애니메이션
    private final Animation workingAnimation;

    // 현재 오브젝트가 collision object가 되었는지 판단하는 것.
    private boolean isTouch = false;
    private final BufferedImage touchSprite = Assets.TILEMAP[20];

    // 음식을 저장할 수 있는 수.
    private final Food[] holdFood = new Food[4];
    private final int holdFoodMax;
    private int holdFoodIndex = 0;

    // 작동 관련
    protected final double progressMax = 100;
    protected double progressValue = 0;
    private final BufferedImage progressSprite = Assets.TILEMAP[25];

    // 비어 있어서 음식을 세팅할 수 있음
    protected WorkState workState = WorkState.NONE;

    public InteractionBlock(int x, int y, int width, int height, BufferedImage sprite, Animation animation, int holdFoodMax) {
        super(x, y, width, height, sprite, DepthType.BIGOBJ, true);
        this.holdFoodMax = holdFoodMax;
        this.workingAnimation = animation;
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
        return holdFoodIndex > 0;
    }

    public boolean isCanAdd() {
        return (holdFoodIndex < holdFoodMax);
    }

    public boolean isHoldFood() {
        return holdFoodIndex > 0;
    }

    public RenderData getTouchRenderData() {
        setTouch(false);
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), touchSprite, DepthType.UI);
    }

    public RenderData getProgressRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), (int) (getWidth() * (progressValue / progressMax)), getHeight(), progressSprite, DepthType.UI);
    }

    public void addProgress(double value) {
        progressValue += value;
    }

    public abstract void action();

    public abstract void update();

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
