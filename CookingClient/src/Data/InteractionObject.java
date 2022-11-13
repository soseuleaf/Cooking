package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public class InteractionObject extends GameObject {
    private boolean isTouched = false;
    private Animation idle;
    private Animation touched;

    public InteractionObject(int x, int y) {
        super(x, y);
        this.idle = new Animation(99, Assets.tile_array[72]);
        this.touched = new Animation(99, Assets.tile_array[99]);
        this.isSolid = true;
    }

    public void setTouched(boolean value) {
        isTouched = value;
    }

    @Override
    public BufferedImage getSprite() {
        if (isTouched) {
            currentAnimation = touched;
            isTouched = false;
        } else {
            currentAnimation = idle;
        }

        if (currentAnimation == null) return Assets.tile_array[511];
        else {
            return currentAnimation.getCurrentFrame();
        }
    }
}
