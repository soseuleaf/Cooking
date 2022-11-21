package Component;

import Component.Type.FoodType;
import Component.Type.DepthType;
import Component.Base.Object;
import Component.Static.Config;
import Component.Static.Assets;
import Component.DTO.ImageRenderData;
import Component.DTO.RenderData;
import Component.DTO.StringRenderData;
import lombok.Getter;


public class Food extends Object implements Cloneable {
    @Getter
    private final FoodType foodType;
    private boolean isCharacter = false;

    public Food(FoodType foodType) {
        super(0, 0, Config.TileSize, Config.TileSize, Assets.DISHMAP[foodType.getSpriteNum()], DepthType.EFFECT);
        this.foodType = foodType;
    }

    public int getId() {
        return foodType.ordinal();
    }

    public String getName() {
        return foodType.getName();
    }

    public void setParentPosition(int x, int y, boolean isCharacter) {
        this.x = x;
        this.y = y;
        this.isCharacter = isCharacter;
    }

    public RenderData getStringRenderData() {
        return new StringRenderData(x + Config.TileSize - getWidth() + 10, y + Config.TileSize - 10, getName());
    }

    @Override
    public Food clone() {
        try {
            return (Food) super.clone();
        } catch (Exception e) {
            System.out.println(e);
            return new Food(FoodType.TEST);
        }
    }

    // 플레이어가 들 때와 블록위에 두었을 때 이미지를 다르게 설정 함.
    @Override
    public RenderData getImageRenderData() {
        if (isCharacter) {
            int posY = this.y - Config.TileSize;
            return new ImageRenderData(x + Config.TileSize - getWidth(), posY + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), DepthType.EFFECT);
        } else {
            return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
        }
    }
}
