package Component.Base;

import Component.Block;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.Food;
import Component.Packet.BlockPacket;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.BlockType;
import Component.Type.DepthType;
import Component.Type.FoodType;
import Component.Type.WorkState;
import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
public abstract class InteractionBlock extends Block {
    //
    protected BlockType blockType;

    // 현재 오브젝트가 collision object가 되었는지 판단하는 것.
    private boolean isTouch = false;
    private final BufferedImage touchSprite = Assets.touchBlock;

    // 음식을 저장할 수 있는 수.
    protected Food[] holdFood = new Food[4];
    protected int holdFoodMax;
    private int holdFoodIndex = 0;

    // 작동 관련
    protected final double progressMax = 100;
    protected double progressValue = 0;
    private final BufferedImage progressSprite = Assets.progressBlock;

    // 비어 있어서 음식을 세팅할 수 있음
    protected WorkState workState = WorkState.NONE;

    public InteractionBlock(int x, int y, int width, int height, BufferedImage sprite, BlockType blockType, int holdFoodMax) {
        super(x, y, width, height, sprite, DepthType.OBJECT, true);
        this.holdFoodMax = holdFoodMax;
        this.blockType = blockType;
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

    public void clearFood() {
        holdFood = new Food[4];
        holdFoodIndex = 0;
    }

    public FoodType[] getFoodToFoodType() {
        FoodType[] foodTypes = new FoodType[holdFoodIndex];
        for (int i = 0; i < holdFoodIndex; i++) {
            foodTypes[i] = holdFood[i].getFoodType();
        }
        return foodTypes;
    }

    public void setEventData(BlockPacket blockPacket) {
        int i = 0;
        for (FoodType foodType : blockPacket.getFoodType()) {
            Food food = Assets.FOODLIST.get(foodType.ordinal()).clone();
            food.setParentPosition(getX(), getY(), false);
            holdFood[i++] = food;
        }
        holdFoodIndex = i;
        this.progressValue = blockPacket.getProgress();
        this.workState = blockPacket.getWorkState();
    }

    public boolean isHoldFood() {
        return holdFoodIndex > 0;
    }

    public RenderData getTouchRenderData() {
        setTouch(false);
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), touchSprite, DepthType.EFFECT);
    }

    public RenderData getProgressRenderData() {
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), (int) (getWidth() * (progressValue / progressMax)), getHeight(), progressSprite, DepthType.EFFECT);
    }

    public RenderData getNeedActionRenderData() {
        return new ImageRenderData(x, y + 1, Config.TileSize, Config.TileSize, Assets.actionIcon, DepthType.EFFECT);
    }

    public void addProgress(double value) {
        progressValue += value;
    }

    public boolean canPop() {
        return workState == WorkState.DONE;
    }

    public boolean canAdd() {
        return workState == WorkState.NONE;
    }

    public boolean canAction() {
        return workState == WorkState.NEEDACTION;
    }

    public abstract void playSoundEffect();

    public abstract boolean canFood(Food food);

    public abstract void action();

    public abstract void update();

    public abstract RenderData getFoodRenderData(int index);

    public abstract RenderData getImageRenderData();
}
