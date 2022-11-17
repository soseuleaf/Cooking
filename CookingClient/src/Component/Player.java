package Component;

import Component.Type.EventType;
import Component.Base.Character;
import Component.Base.InteractionBlock;
import Component.Static.Config;
import Component.DTO.RenderData;
import Component.DTO.StringRenderData;
import Component.Type.WorkState;
import lombok.Getter;

public class Player extends Character {
    private final int speed = 20;
    private int moveX;
    private int moveY;
    private boolean onSpace = false;

    @Getter
    private Block collisionBlock = null;
    private final long actionCooltimeMax = 1000;
    private long actionCooltime = 0;
    private final long messageBoxTimeMax = 1000;
    private long messageBoxTime = 0;
    private String message = null;
    private long actionLastTime = 0;
    private long messageLastTime = 0;

    public Player() {
        super(300, 200, "ME");
    }

    public void setMoveX(int value) {
        moveX = value * speed;
    }

    public void setMoveY(int value) {
        moveY = value * speed;
    }

    public void updateMove(Block[] aroundBox) {
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

            if (isHold()) peekFood().setParentPosition(x, y, true);
        }
    }

    public Boolean updateAction() {
        if (collisionBlock instanceof InteractionBlock interactionBlock) {
            interactionBlock.setTouch(true);
        }

        if (!onSpace) return false;
        onSpace = false;

        if (collisionBlock instanceof Table table) {
            if (table.isCanPop() && canHold()) {
                addFood(table.popFood());
                return true;
            } else if (table.isCanAdd() && isHold()) {
                table.addFood(popFood());
                return true;
            } else if (!table.isHoldFood()) {
                addMessage("아무것도 없어.");
            } else if (!table.isCanPop()) {
                addMessage("여기선 할 수 없어.");
            } else if (!canHold()) {
                addMessage("여기에 넣을 수 없어.");
            }
            return false;
        } else if (collisionBlock instanceof FoodBox foodBox) {
            if (canHold() && foodBox.isCanPop()) {
                addFood(foodBox.popFood());
                return true;
            }
        } else if (collisionBlock instanceof Knife knife) {
            if (isHold() && knife.getWorkState() == WorkState.NONE) {
                knife.addFood(popFood());
                return true;
            } else if (!isHold() && knife.getWorkState() == WorkState.WORKING) {
                knife.action();
                return true;
            } else if (!isHold() && knife.getWorkState() == WorkState.DONE) {
                addFood(knife.getSlicedFood());
                return true;
            } else {
                addMessage("음식을 들고있지 않아");
                return false;
            }
        } else {
            addMessage("할 수 없어.");
        }

        return false;
    }

    public void onSpace() {
        actionCooltime += System.currentTimeMillis() - actionLastTime;
        actionLastTime = System.currentTimeMillis();
        onSpace = false;

        if (actionCooltime > actionCooltimeMax) {
            actionCooltime = 0;
            onSpace = true;
        } else if (actionCooltime > actionCooltimeMax / 2) { // 너무 민감해서 일정 시간이상 지났을 때만 작동
            addMessage("조금만 더 기다려줘.");
        }
    }

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
        return new StringRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight() - Config.TileSize / 2, message);
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
}
