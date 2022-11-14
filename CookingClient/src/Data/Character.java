package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public abstract class Character extends Object {
    protected boolean isLookRight = true;
    private final String name;
    protected Animation animLeft = new Animation(50, Assets.CHARACTER_MOVE_LEFT);
    protected Animation animRight = new Animation(50, Assets.CHARACTER_MOVE_RIGHT);
    protected Animation animIdleLeft = new Animation(999, Assets.CHARACTER_IDLE_LEFT);
    protected Animation animIdleRight = new Animation(999, Assets.CHARACTER_IDLE_RIGHT);
    protected Animation currentAnimation = null;

    public Character(int x, int y, String name) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
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
