package Data;

public class Player extends Character {
    private final int speed = 8;
    private int moveX;
    private int moveY;
    private boolean onSpace = false;
    private Block collisionBlock = null;
    private long actionCooltimeMax = 1000;
    private long actionCooltime = 0;
    private long messageBoxTimeMax = 1000;
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

    public int collisionBlockTileX() {
        return collisionBlock.getTileX();
    }

    public int collisionBlockTileY() {
        return collisionBlock.getTileY();
    }

    public EventEnum updateData(Block[] aroundBox) {
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

            if (isHoldFood()) peekFood().setParentPosition(x, y, true);
        }

        if (collisionBlock instanceof InteractionBlock interactionObject) {
            interactionObject.setTouch(true);
        }

        if (onSpace && (collisionBlock != null)) {
            onSpace = false;
            if (collisionBlock.canHold && collisionBlock.isHoldFood() && !this.isHoldFood()) {
                Food food = collisionBlock.popFood();
                this.addFood(food);
                return EventEnum.FOOD_PUT;
            } else if (collisionBlock.canHold && !collisionBlock.isHoldFood() && this.isHoldFood()) {
                Food food = this.popFood();
                collisionBlock.addFood(food);
                return EventEnum.FOOD_DOWN;
            } else if (collisionBlock.isHoldFood() && this.isHoldFood()) {
                addMessage("이미 물건을 들고 있어");
            }
        }

        return EventEnum.NONE;
    }

    public void onSpace(boolean space) {
        actionCooltime += System.currentTimeMillis() - actionLastTime;
        actionLastTime = System.currentTimeMillis();

        if (actionCooltime > actionCooltimeMax) {
            actionCooltime = 0;
            onSpace = space;
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

        int boundX = Config.TileSize - 16;
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
