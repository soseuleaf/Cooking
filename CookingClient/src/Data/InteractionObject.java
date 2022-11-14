package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public class InteractionObject extends Object {
    private boolean isTouched = false;
    private boolean isUsed = false;
    private final BufferedImage touchSprite;
    private final Animation interaction;

    public InteractionObject(int x, int y, int width, int height, BufferedImage sprite, BufferedImage touchSprite) {
        super(x, y, sprite, RenderDepth.OBJECT, true);
        this.touchSprite = touchSprite;
        this.interaction = new Animation(99, Assets.TILEMAP[99]);
        setSize(width, height);
    }

    public void setTouched(boolean value) {
        isTouched = value;
    }

    @Override
    public BufferedImage getSprite() {
        if (isUsed) {
            return interaction.getCurrentFrame();
        } else if (isTouched) {
            isTouched = false;
            return touchSprite;
        } else {
            return sprite;
        }
    }
}
