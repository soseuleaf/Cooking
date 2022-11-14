package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public abstract class Character {
    protected final String name;
    private final RenderDepth depth = RenderDepth.OBJECT;
    protected int x;
    protected int y;
    protected Animation animLeft;
    protected Animation animRight;
    protected Animation animIdleLeft;
    protected Animation animIdleRight;
    protected Animation currentAnimation = null;

    public Character(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        animLeft = new Animation(50, Assets.CHARACTER_MOVE_LEFT);
        animRight = new Animation(50, Assets.CHARACTER_MOVE_RIGHT);
        animIdleLeft = new Animation(999, Assets.CHARACTER_IDLE_LEFT);
        animIdleRight = new Animation(999, Assets.CHARACTER_IDLE_RIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public RenderDepth getDepth() {
        return depth;
    }

    public abstract BufferedImage getSprite();

    public String getName() {
        return name;
    }
}
