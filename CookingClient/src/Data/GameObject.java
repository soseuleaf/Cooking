package Data;

import Render.Animation;

import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected boolean isSolid = false;
    protected int boundX = Config.CharacterSize;
    protected int boundY = Config.CharacterSize;
    protected RenderDepth depth = RenderDepth.OBJECT;
    protected Animation currentAnimation = null;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
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
}
