package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public class Player extends GameObject {
    private final int speed = 8;
    private final String name;
    private final Animation animLeft;
    private final Animation animRight;
    private final Animation animIdleLeft;
    private final Animation animIdleRight;
    private int moveX = 0;
    private int moveY = 0;
    private boolean isLookRight = true;
    private GameObject collisionObject = null;

    public Player() {
        super(200, 200);
        this.name = "ME";
        this.boundX = Config.TileSize - 32;
        this.boundY = 1;
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

    public void updateMove(GameObject[] aroundObject) {
        if (moveX != 0 || moveY != 0) {
            collisionObject = null;

            x += moveX;
            for (int index = 0; index < aroundObject.length; index++) {
                if (isCollision(aroundObject[index])) {
                    x -= moveX;
                    collisionObject = aroundObject[index];
                    break;
                }
            }

            y += moveY;
            for (int index = 0; index < aroundObject.length; index++) {
                if (isCollision(aroundObject[index])) {
                    y -= moveY;
                    collisionObject = aroundObject[index];
                    break;
                }
            }
        }

        if (collisionObject instanceof InteractionObject interactionObject) {
            interactionObject.setTouched(true);
        }

        // 굳이 여기서 애니메이션 처리를 하는 이유는 movexy를 zero로 하면 updateAnimation에서 애니메이션을 못 찾음
        updateAnimation();
        setMoveZero();
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

    private boolean isCollision(GameObject other) { // 충돌 체크 함수
        if (!other.isSolid) return false;

        int thisCenterX = x + (Config.TileSize / 2);
        int thisCenterXLeft = thisCenterX - (boundX / 2);

        int thisCenterY = y + (Config.TileSize / 2);
        int thisCenterYUp = thisCenterY - (boundY / 2);

        int otherCenterX = other.x + (Config.TileSize / 2);
        int otherCenterXLeft = otherCenterX - (other.boundX / 2);

        int otherCenterY = other.y + (Config.TileSize / 2);
        int otherCenterYUp = otherCenterY - (other.boundY / 2);

        return thisCenterXLeft + boundX >= otherCenterXLeft &&
                thisCenterXLeft <= otherCenterXLeft + other.boundX &&
                thisCenterYUp + boundY >= otherCenterYUp &&
                thisCenterYUp <= otherCenterYUp + other.boundY;
    }

    @Override
    public BufferedImage getSprite() {
        if (currentAnimation == null) return Assets.tile_array[511];
        else {
            return currentAnimation.getCurrentFrame();
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
