package Data;

public class Player extends Character {
    private final int speed = 8;
    private int moveX;
    private int moveY;
    private Block collisionBlock = null;

    public Player() {
        super(200, 200, "ME");
    }

    public void setMoveX(int value) {
        moveX = value * speed;
    }

    public void setMoveY(int value) {
        moveY = value * speed;
    }

    public void updateData(Block[] aroundBox) {
        if (moveX != 0 || moveY != 0) {
            collisionBlock = null;

            x += moveX;
            for (int index = 0; index < aroundBox.length; index++) {
                if (isCollision(aroundBox[index])) {
                    x -= moveX;
                    collisionBlock = aroundBox[index];
                    break;
                }
            }

            y += moveY;
            for (int index = 0; index < aroundBox.length; index++) {
                if (isCollision(aroundBox[index])) {
                    y -= moveY;
                    collisionBlock = aroundBox[index];
                    break;
                }
            }
        }

        if (collisionBlock instanceof InteractionBlock interactionObject) {
            interactionObject.setTouch(true);
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

    private boolean isCollision(Block other) { // 충돌 체크 함수
        if (other == null || !other.isSolid()) return false;

        int boundX = Config.TileSize - 32;
        int boundY = 2;

        int thisCenterX = x + (getWidth() / 2);
        int thisCenterXLeft = thisCenterX - (boundX / 2);

        int thisCenterY = y + (getHeight() / 2);
        int thisCenterYUp = thisCenterY - (boundY / 2);

        int otherCenterX = other.getX() + (Config.TileSize / 2);
        int otherCenterXLeft = otherCenterX - (Config.TileSize / 2);

        int otherCenterY = other.getY() + (Config.TileSize / 2);
        int otherCenterYUp = otherCenterY - (Config.TileSize / 2);

        return thisCenterXLeft + boundX >= otherCenterXLeft &&
                thisCenterXLeft <= otherCenterXLeft + Config.TileSize &&
                thisCenterYUp + boundY >= otherCenterYUp &&
                thisCenterYUp <= otherCenterYUp + Config.TileSize;
    }
}
