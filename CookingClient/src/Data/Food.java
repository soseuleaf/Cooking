package Data;

import Render.Assets;

import java.awt.image.BufferedImage;

public class Food extends Object implements Cloneable {
    private int id;
    private String name;
    boolean isOriginal = true;
    boolean isFire = false;
    boolean isKnife = false;
    boolean isCharacter = false;
    BufferedImage sprite;

    public Food(FoodType food, BufferedImage sprite) {
        super(0, 0);
        this.id = food.getIndex();
        this.name = food.getName();
        setSprite(sprite);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setParentPosition(int x, int y, boolean isCharacter) {
        this.x = x;
        this.y = y;
        this.isCharacter = isCharacter;

    }

    public RenderData getStringRenderData() {
        return new StringRenderData(x + Config.TileSize - getWidth() + 10, y + Config.TileSize - 10, Integer.toString(id));
    }

    @Override
    public Food clone() {
        try {
            return (Food) super.clone();
        } catch (Exception e) {
            System.out.println(e);
            return new Food(FoodType.TEST, Assets.TEST);
        }
    }

    @Override
    public RenderData getImageRenderData() {
        if (isCharacter) {
            int posY = this.y - Config.TileSize;
            return new ImageRenderData(x + Config.TileSize - getWidth(), posY + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), RenderDepth.EFFECT);
        } else {
            return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), getSprite(), getDepth());
        }
    }
}
