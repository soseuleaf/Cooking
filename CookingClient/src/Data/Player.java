package Data;

import Render.Assets;

import java.awt.image.BufferedImage;

public class Player extends Character {
    private final int speed = 8;
    private final int boundX = Config.TileSize - 32;
    private final int boundY = 1;
    private int moveX;
    private int moveY;
    private boolean isLookRight = true;
    private Object collisionObject = null;

    public Player() {
        super("ME", 200, 200);
    }

    public void setMoveX(int value) {
        moveX = value * speed;
    }

    public void setMoveY(int value) {
        moveY = value * speed;
    }

    public void updateData(Object[] aroundObject) {
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

    private boolean isCollision(Object other) { // 충돌 체크 함수
        if (other == null || !other.isSolid()) return false;

        int thisCenterX = x + (Config.TileSize / 2);
        int thisCenterXLeft = thisCenterX - (boundX / 2);

        int thisCenterY = y + (Config.TileSize / 2);
        int thisCenterYUp = thisCenterY - (boundY / 2);

        int otherCenterX = other.getX() + (Config.TileSize / 2);
        int otherCenterXLeft = otherCenterX - (other.getBoundX() / 2);

        int otherCenterY = other.getY() + (Config.TileSize / 2);
        int otherCenterYUp = otherCenterY - (other.getBoundY() / 2);

        return thisCenterXLeft + boundX >= otherCenterXLeft &&
                thisCenterXLeft <= otherCenterXLeft + other.getBoundX() &&
                thisCenterYUp + boundY >= otherCenterYUp &&
                thisCenterYUp <= otherCenterYUp + other.getBoundY();
    }

    @Override
    public BufferedImage getSprite() {
        if (currentAnimation == null) return Assets.TILEMAP[511];
        else {
            return currentAnimation.getCurrentFrame();
        }
    }
}
