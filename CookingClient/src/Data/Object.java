package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public class Object {
    protected int x = 100;
    protected int y = 100;
    protected RenderDepth depth = RenderDepth.OBJECT;
    Animation currentAnimation = null;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public RenderDepth getDepth() {
        return depth;
    }

    public BufferedImage getSprite() {
        if (currentAnimation == null)
            return Assets.idle_left[0];
        else {
            currentAnimation.update();
            return currentAnimation.getCurrentFrame();
        }
    }
}
