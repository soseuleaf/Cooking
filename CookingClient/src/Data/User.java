package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public class User extends Character {
    private boolean isLookRight = true;

    public User(String name, int x, int y) {
        super(name, x, y);
        animLeft = new Animation(50, Assets.CHARACTER_MOVE_LEFT);
        animRight = new Animation(50, Assets.CHARACTER_MOVE_RIGHT);
        animIdleLeft = new Animation(999, Assets.CHARACTER_IDLE_LEFT);
        animIdleRight = new Animation(999, Assets.CHARACTER_IDLE_RIGHT);
    }



    public BufferedImage getSprite() {
        if (currentAnimation == null)
            return Assets.BLACKTILE;
        else {
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
