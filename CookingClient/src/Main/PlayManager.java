package Main;

import Component.Base.InteractionBlock;
import Component.*;
import Component.DTO.ImageRenderData;
import Component.DTO.KeyEventData;
import Component.DTO.StringRenderData;
import Component.Packet.BlockPacket;
import Component.Packet.EventPacket;
import Component.Packet.StatePacket;
import Component.Render.AssetLoader;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.DepthType;
import Component.Type.FoodType;
import Component.Type.WorkState;

import java.util.ArrayList;
import java.util.Scanner;

public class PlayManager {
    // 오브젝트 넘기기 위해 저장
    private final CookTogether cookTogether;

    // 맵 관련
    private final Block[] aroundObject = new Block[9];
    private final Block[][] backgrondMap = new Block[Config.MAP_Y][Config.MAP_X];
    private final Block[][] objectMap = new Block[Config.MAP_Y][Config.MAP_X];

    // 플레이어 관련
    private final Player player;

    // 주문 관련
    private final ArrayList<Order> orderArrayList = new ArrayList<>();

    private boolean viewUi = false;

    // 스코어 관련
    private int score = 0;
    private double time = 300;

    public PlayManager(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
        this.player = new Player();
        loadWorld();
    }

    private void loadWorld() {
        Scanner floorScanner;
        Scanner solidScanner;

        try {
            floorScanner = new Scanner(AssetLoader.loadText(Config.BACKGROUNDMAP));
            solidScanner = new Scanner(AssetLoader.loadText(Config.BLOCKMAP));

            for (int y = 0; y < Config.MAP_Y; y++) {
                for (int x = 0; x < Config.MAP_X; x++) {
                    int tileX = Config.TileSize * x;
                    int tileY = Config.TileSize * y;
                    int backgrond = floorScanner.nextInt();
                    int solid = solidScanner.nextInt();

                    if (backgrond != -1) {
                        backgrondMap[y][x] = new Block(tileX, tileY, Config.TileSize, Config.TileSize, Assets.TILEMAP[backgrond], DepthType.MAP, false);
                    }

                    switch (solid) {
                        case 0 ->
                                objectMap[y][x] = new Block(tileX, tileY, Config.TileSize, Config.TileSize, Assets.TILEMAP[backgrond], DepthType.OBJECT, true);
                        case 1 -> objectMap[y][x] = new Table(tileX, tileY, null);
                    }
                }
            }

            objectMap[3][1] = new FoodBox(Config.TileSize, Config.TileSize * 3, Assets.FOODLIST.get(12).clone());
            objectMap[3][2] = new FoodBox(Config.TileSize * 2, Config.TileSize * 3, Assets.FOODLIST.get(16).clone());
            objectMap[3][3] = new FoodBox(Config.TileSize * 3, Config.TileSize * 3, Assets.FOODLIST.get(14).clone());
            objectMap[3][4] = new FoodBox(Config.TileSize * 4, Config.TileSize * 3, Assets.FOODLIST.get(6).clone());
            objectMap[3][5] = new Pot(Config.TileSize * 5, Config.TileSize * 3);

            objectMap[3][7] = new FoodBox(Config.TileSize * 7, Config.TileSize * 3, Assets.FOODLIST.get(9).clone());
            objectMap[3][8] = new FoodBox(Config.TileSize * 8, Config.TileSize * 3, Assets.FOODLIST.get(0).clone());
            objectMap[3][9] = new Fryer(Config.TileSize * 9, Config.TileSize * 3, true);
            objectMap[3][10] = new Fryer(Config.TileSize * 10, Config.TileSize * 3, false);
            objectMap[3][11] = new FoodBox(Config.TileSize * 11, Config.TileSize * 3, Assets.FOODLIST.get(4).clone());
            objectMap[3][12] = new FoodBox(Config.TileSize * 12, Config.TileSize * 3, Assets.FOODLIST.get(2).clone());

            objectMap[3][14] = new Frypan(Config.TileSize * 14, Config.TileSize * 3);
            objectMap[3][15] = new Frypan(Config.TileSize * 15, Config.TileSize * 3);
            objectMap[3][16] = new FoodBox(Config.TileSize * 16, Config.TileSize * 3, Assets.FOODLIST.get(18).clone());
            objectMap[3][17] = new FoodBox(Config.TileSize * 17, Config.TileSize * 3, Assets.FOODLIST.get(20).clone());
            objectMap[3][18] = new FoodBox(Config.TileSize * 18, Config.TileSize * 3, Assets.FOODLIST.get(22).clone());

            objectMap[8][0] = new Knife(0, Config.TileSize * 8);
            objectMap[8][6] = new Knife(Config.TileSize * 6, Config.TileSize * 8);
            objectMap[8][13] = new Knife(Config.TileSize * 13, Config.TileSize * 8);
            objectMap[8][19] = new Knife(Config.TileSize * 19, Config.TileSize * 8);


            objectMap[10][0] = new Trash(0, Config.TileSize * 10);
            objectMap[10][19] = new Trash(Config.TileSize * 19, Config.TileSize * 10);
            objectMap[11][0] = new Trash(0, Config.TileSize * 11);
            objectMap[11][19] = new Trash(Config.TileSize * 19, Config.TileSize * 11);

            objectMap[11][7] = new FoodOut(Config.TileSize * 7, Config.TileSize * 11);
            objectMap[11][8] = new FoodOut(Config.TileSize * 8, Config.TileSize * 11);
            objectMap[11][9] = new FoodOut(Config.TileSize * 9, Config.TileSize * 11);
            objectMap[11][10] = new FoodOut(Config.TileSize * 10, Config.TileSize * 11);
            objectMap[11][11] = new FoodOut(Config.TileSize * 11, Config.TileSize * 11);
            objectMap[11][12] = new FoodOut(Config.TileSize * 12, Config.TileSize * 11);


        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void update(KeyEventData keyEventData) {
        // 키 입력 전송
        if (keyEventData.w()) player.setMoveY(-1);
        if (keyEventData.a()) player.setMoveX(-1);
        if (keyEventData.s()) player.setMoveY(1);
        if (keyEventData.d()) player.setMoveX(1);
        if (keyEventData.q()) viewUi = !viewUi;

        player.onSpace(keyEventData.space());

        // 주변 블록 확인
        checkAroundBlock(player.getTileX(), player.getTileY());

        // 플레이어 데이터 업데이트
        player.updateMove(aroundObject);

        // 블록 업데이트를 통한 블록 동기화 패킷 전송 
        if (player.updateAction()) {
            InteractionBlock sendBlock = (InteractionBlock) player.getCollisionBlock();
            cookTogether.sendBlockPacket(
                    sendBlock.getBlockType(),
                    sendBlock.getTileY(),
                    sendBlock.getTileX(),
                    sendBlock.getFoodToFoodType(),
                    sendBlock.getWorkState(),
                    sendBlock.getProgressValue()
            );

            if (sendBlock instanceof FoodOut) {
                for (Order order : orderArrayList) {
                    if (order.getFoodType() == sendBlock.peekFood().getFoodType()) {
                        cookTogether.sendEventPacket(order.getUuid(), order.getFoodType());
                        break;
                    }
                }

            }
        }

        player.updateAnimation();
        player.controlMessageBox();

        FoodType foodType = null;
        if (player.peekFood() != null) {
            foodType = player.peekFood().getFoodType();
        }
        // 데이터 전송
        cookTogether.sendUserPacket(player.getX(), player.getY(), foodType);

        // 데이터 정리
        player.setMoveX(0);
        player.setMoveY(0);
    }

    public void checkAroundBlock(int tileX, int tileY) {
        if (tileY < 0) tileY = 0;
        if (tileY > Config.MAP_Y) tileY = Config.MAP_Y;
        if (tileX < 0) tileX = 0;
        if (tileX > Config.MAP_X) tileX = Config.MAP_X;

        aroundObject[0] = objectMap[tileY - 1][tileX - 1];
        aroundObject[1] = objectMap[tileY - 1][tileX];
        aroundObject[2] = objectMap[tileY - 1][tileX + 1];
        aroundObject[3] = objectMap[tileY][tileX - 1];
        aroundObject[4] = objectMap[tileY][tileX];
        aroundObject[5] = objectMap[tileY][tileX + 1];
        aroundObject[6] = objectMap[tileY + 1][tileX - 1];
        aroundObject[7] = objectMap[tileY + 1][tileX];
        aroundObject[8] = objectMap[tileY + 1][tileX + 1];
    }

    public void updatePlayerRender() {
        cookTogether.addRenderData(player.getImageRenderData());
        cookTogether.addRenderData(player.getStringRenderData());

        if (player.isHold()) {
            cookTogether.addRenderData(player.peekFood().getImageRenderData());
        }
        if (player.canMessageRender()) {
            cookTogether.addRenderData(player.getMessageRenderData());
        }
    }

    public void updateMapRender() {
        for (int y = 0; y < Config.MAP_Y; y++) {
            for (int x = 0; x < Config.MAP_X; x++) {
                Block background = backgrondMap[y][x];
                Block object = objectMap[y][x];

                if (background != null) {
                    cookTogether.addRenderData(background.getImageRenderData());
                }

                if (object != null) {
                    if (object instanceof InteractionBlock obj) {
                        obj.update();
                        cookTogether.addRenderData(obj.getImageRenderData());
                        if (obj.getProgressValue() > 0)
                            cookTogether.addRenderData(obj.getProgressRenderData());
                        if (obj.getWorkState() == WorkState.NEEDACTION)
                            cookTogether.addRenderData(obj.getNeedActionRenderData());
                        if (obj.isTouch())
                            cookTogether.addRenderData(obj.getTouchRenderData());
                        if (obj.isHoldFood()) {
                            for (int i = 0; i < obj.getHoldFoodIndex(); i++) {
                                cookTogether.addRenderData(obj.getFoodRenderData(i));
                            }
                        }
                    } else {
                        cookTogether.addRenderData(object.getImageRenderData());
                    }
                }
            }
        }
    }


    public void updateUiRender() {
        int[] timeArray = new int[4];
        timeArray[0] = (int) time / 60 / 10;
        timeArray[1] = (int) time / 60 % 10;
        timeArray[2] = (int) time % 60 / 10;
        timeArray[3] = (int) time % 60 % 10;

        int[] timePosX = new int[4];
        timePosX[0] = Config.DisplayWidth / 2 - (Config.TileSize * 2) - (Config.TileSize / 2);
        timePosX[1] = timePosX[0] + Config.TileSize;
        timePosX[2] = timePosX[1] + Config.TileSize + Config.TileSize;
        timePosX[3] = timePosX[2] + Config.TileSize;
        int timePosY = 16;

        // 시간 그리기
        int timeCenter = Config.DisplayWidth / 2 - (Config.TileSize / 2);
        cookTogether.addRenderData(new ImageRenderData(timeCenter, timePosY, Config.TileSize, Config.TileSize, Assets.NUMBER[10], DepthType.UI));

        for (int i = 0; i < 4; i++) {
            int tmp = Math.max(timeArray[i], 0);
            cookTogether.addRenderData(new ImageRenderData(timePosX[i], timePosY, Config.TileSize, Config.TileSize, Assets.NUMBER[tmp], DepthType.UI));
        }

        // 점수 그리기 임시
        cookTogether.addRenderData(new StringRenderData(Config.DisplayWidth / 2, 200, String.valueOf(score)));

        // 오더 그리기
        if (viewUi) {
            int posX = 100;
            int posY = (int) (Config.DisplayHeight - (Config.OrderUiSize * 1.1));

            for (Order order : orderArrayList) {
                int posCenterX = posX + (int) (Config.OrderUiSize * 0.5);
                int posCenterY = posY + (int) (Config.OrderUiSize * 0.5);
                int posCenterXL = posCenterX - (int) (Config.TileSize * 0.5);
                int posCenterYU = posCenterY - (int) (Config.TileSize * 0.5);

                cookTogether.addRenderData(new ImageRenderData(posX, posY, Config.OrderUiSize, Config.OrderUiSize, Assets.order, DepthType.UI));
                cookTogether.addRenderData(new ImageRenderData(posCenterXL, posCenterYU, Config.TileSize, Config.TileSize, Assets.DISHMAP[order.getFoodType().getSpriteNum()], DepthType.UI));
                cookTogether.addRenderData(new ImageRenderData(posX, posY, (int) (Config.OrderUiSize * (order.getNowTime() / order.getMaxTime())), Config.OrderUiSize, Assets.orderTime, DepthType.UI));
                posX += Config.OrderUiSize * 1.1;
            }
        }
    }


    public void recvBlockPacket(BlockPacket blockPacket) {
        if (objectMap[blockPacket.getY()][blockPacket.getX()] instanceof InteractionBlock block) {
            block.setEventData(blockPacket);
        } else {
            // TODO: 혹시나 해당 블록이 없으면 동작하는 함수, 나중에 천천히 업데이트
            Block temp;
            switch (blockPacket.getBlockType()) {
                case Table -> temp = new Table(blockPacket.getX(), blockPacket.getY(), Assets.DISHMAP[0]);
                case FoodBox ->
                        temp = new FoodBox(blockPacket.getX(), blockPacket.getY(), Assets.FOODLIST.get(blockPacket.getFoodType()[0].ordinal()).clone());
                case Knife -> temp = new Knife(blockPacket.getX(), blockPacket.getY());
                case Pot -> temp = new Pot(blockPacket.getX(), blockPacket.getY());
                default ->
                        temp = new Block(blockPacket.getX(), blockPacket.getY(), Config.TileSize, Config.TileSize, Assets.BLACKTILE, DepthType.OBJECT, true);
            }
            objectMap[blockPacket.getY()][blockPacket.getX()] = temp;
        }
        ((InteractionBlock) objectMap[blockPacket.getY()][blockPacket.getX()]).setEventData(blockPacket);
    }

    public void recvEventPacket(EventPacket eventPacket) {
        switch (eventPacket.getCode()) {
            case 10 -> orderArrayList.add(Order.NewOrder(eventPacket.getOrderUuid(), eventPacket.getFoodType()));
            case 20 -> System.out.println("이 패킷이 왜 여기에?");
            case 30 -> orderArrayList.removeIf(order -> order.getUuid().equals(eventPacket.getOrderUuid()));
            default -> System.out.println("알 수 없는 이벤트 패킷");
        }
    }

    public void recvStatePacket(StatePacket statePacket) {
        double passedTime = time - statePacket.getTime();
        this.time = statePacket.getTime();
        this.score = statePacket.getScore();
        System.out.println(time + ", " + score + orderArrayList);
        orderArrayList.replaceAll(order -> order.updateTime(passedTime));
    }
}
