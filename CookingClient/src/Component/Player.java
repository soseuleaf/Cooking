package Component;

import Component.Base.Character;
import Component.Base.InteractionBlock;
import Component.DTO.RenderData;
import Component.DTO.StringRenderData;
import Component.Static.Config;
import lombok.Getter;

public class Player extends Character {
    private int speed = 12;
    private int moveX;
    private int moveY;
    private boolean onSpace = false;

    @Getter
    private Block collisionBlock = null;

    public Player(int index, String name) {
        super(index, name, 300, 400);
    }

    /* 데이터 설정 */
    public void setSpace(boolean space) {
        onSpace = space;
    }

    public void setMoveX(int value) {
        moveX = value * speed;
    }

    public void setMoveY(int value) {
        moveY = value * speed;
    }

    public void setSpeed(int value) {
        speed = value;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void removeFood() {
        this.holdFood = null;
    }

    /* 데이터 업데이트 */

    public void updateMove(Block[] aroundBox) {
        if (moveX != 0 || moveY != 0) {
            collisionBlock = null;

            x += moveX;
            for (Block box : aroundBox) {
                if (isCollision(box)) {
                    x -= moveX;
                    collisionBlock = box;
                    break;
                }
            }

            y += moveY;
            for (Block box : aroundBox) {
                if (isCollision(box)) {
                    y -= moveY;
                    collisionBlock = box;
                    break;
                }
            }

            if (isHold()) peekFood().setParentPosition(x, y, true);
        }
    }

    public Boolean updateAction() {
        if (collisionBlock instanceof InteractionBlock interactionBlock) {
            interactionBlock.setTouch(true);
        }

        if (!onSpace) return false;

        if (collisionBlock instanceof InteractionBlock it) {
            if (isHold() && it.canAdd() && it.canFood(peekFood())) {
                it.addFood(popFood());
                it.playSoundEffect();
                return true;
            } else if (canHold() && it.canPop()) {
                addFood(it.popFood());
                it.playSoundEffect();
                return true;
            } else if (canHold() && it.canAction()) {
                it.action();
                it.playSoundEffect();
                return true;
            }
        }
        return false;
    }

    public void updateAnimation() {
        if (moveX < 0) { //If player is moving left
            isLookedRight = false;
            currentAnimation = animLeft;
        } else if (moveX > 0) { //If player is moving right
            isLookedRight = true;
            currentAnimation = animRight;
        } else if (moveY != 0) { //If player is moving up or down
            if (isLookedRight) currentAnimation = animRight;
            else currentAnimation = animLeft;
        } else { //If player is moving not moving
            if (isLookedRight) {
                currentAnimation = animIdleRight;
            } else {
                currentAnimation = animIdleLeft;
            }
        }
    }

    /* 충돌 체크 */
    private boolean isCollision(Block other) { // 충돌 체크 함수
        if (other == null || !other.isSolid()) return false;

        int boundX = Config.TileSize - (Config.TileSize / 2);
        int boundY = 2;

        int thisCenterX = x + (getWidth() / 2);
        int thisCenterXLeft = thisCenterX - (boundX / 2);

        int thisCenterY = y + (getHeight() / 2);
        int thisCenterYUp = thisCenterY - (boundY / 2);

        int otherCenterX = other.getX() + (Config.TileSize / 2);
        int otherCenterXLeft = otherCenterX - (Config.TileSize / 2);

        int otherCenterY = other.getY() + (Config.TileSize / 2);
        int otherCenterYUp = otherCenterY - (Config.TileSize / 2);

        return thisCenterXLeft + boundX >= otherCenterXLeft && thisCenterXLeft <= otherCenterXLeft + Config.TileSize && thisCenterYUp + boundY >= otherCenterYUp && thisCenterYUp <= otherCenterYUp + Config.TileSize;
    }


    /* 메시지 */
    private final long messageBoxTimeMax = 1000;
    private long messageBoxTime = 0;
    private String message = null;
    private long messageLastTime = 0;

    private void addMessage(String message) {
        this.message = message;
        messageBoxTime = 0;
    }

    public void controlMessageBox() {
        if (message != null) {
            messageBoxTime += System.currentTimeMillis() - messageLastTime;
            messageLastTime = System.currentTimeMillis();
            if (messageBoxTime > messageBoxTimeMax) {
                messageBoxTime = 0;
                message = null;
            }
        }
    }

    public boolean canMessageRender() {
        return message != null;
    }

    public RenderData getMessageRenderData() {
        return new StringRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight() - Config.TileSize / 2, message, 18);
    }
}
