package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public class User extends Object {
    private final String name;
    private final Animation animLeft;
    private final Animation animRight;
    private final Animation animIdleLeft;
    private final Animation animIdleRight;
    private boolean isLookRight = true;

    public User(String name) {
        this.name = name;
        animLeft = new Animation(30, Assets.move_left);
        animRight = new Animation(30, Assets.move_right);
        animIdleLeft = new Animation(999, Assets.idle_left);
        animIdleRight = new Animation(999, Assets.idle_right);
    }

    public String getName() {
        return name;
    }

    public BufferedImage getSprite() {
        if (currentAnimation == null)
            return animIdleLeft.getCurrentFrame();
        else {
            currentAnimation.update();
            return currentAnimation.getCurrentFrame();
        }
    }

    public void setEvent(EventPacket eventPacket) {
        if (eventPacket.x < x) { // If player is moving left
            isLookRight = false;
            currentAnimation = animLeft;
        } else if (eventPacket.x > x) { //If player is moving right
            isLookRight = true;
            currentAnimation = animRight;
        } else if (eventPacket.y != y) { //If player is moving up or down
            if (isLookRight) currentAnimation = animRight;
            else currentAnimation = animLeft;
        } else { //If player is moving not moving
            if (isLookRight) {
                currentAnimation = animIdleRight;
            } else {
                currentAnimation = animIdleLeft;
            }
        }
        this.x = eventPacket.x;
        this.y = eventPacket.y;
    }
}
