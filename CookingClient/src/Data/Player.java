package Data;

import Render.Animation;
import Render.Assets;

public class Player extends Object {
    private final int speed = 8;
    private final String name;
    private final Animation animLeft;
    private final Animation animRight;
    private final Animation animIdleLeft;
    private final Animation animIdleRight;
    private int moveX = 0;
    private int moveY = 0;
    private boolean isLookRight = true;

    public Player() {
        this.name = "ME";
        animLeft = new Animation(50, Assets.move_left);
        animRight = new Animation(50, Assets.move_right);
        animIdleLeft = new Animation(999, Assets.idle_left);
        animIdleRight = new Animation(999, Assets.idle_right);
    }

    public void setMoveX(int value) {
        moveX = value * speed;
    }

    public void setMoveY(int value) {
        moveY = value * speed;
    }

    public String getName() {
        return name;
    }

    public void updateMove() {
        x += moveX;
        y += moveY;
    }

    public void setMoveZero() {
        moveX = 0;
        moveY = 0;
    }

    public void updateAnimation() {
        if (moveX < 0) { //If player is moving left
            isLookRight = false;
            currentAnimation = animLeft;
        } else if (moveX > 0) { //If player is moving right
            isLookRight = true;
            currentAnimation = animRight;
        } else if (moveY != 0) { //If player is moving up or down
            if (isLookRight) currentAnimation = animRight;
            else currentAnimation = animLeft;
        } else { //If player is moving not moving
            if (isLookRight) {
                currentAnimation = animIdleRight;
            } else {
                currentAnimation = animIdleLeft;
            }
        }
    }
}
