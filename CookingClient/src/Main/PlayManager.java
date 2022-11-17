package Main;

import Component.*;
import Component.Packet.BlockPacket;
import Component.Type.DepthType;
import Component.Base.InteractionBlock;
import Component.DTO.KeyEventData;
import Component.Static.Config;
import Component.Render.AssetLoader;
import Component.Static.Assets;
import Component.Type.FoodType;

import java.util.Random;
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
            solidScanner = new Scanner(AssetLoader.loadText(Config.SOLIDMAP));
            Random random = new Random();

            for (int y = 0; y < Config.MAP_Y; y++) {
                for (int x = 0; x < Config.MAP_X; x++) {
                    int tileX = Config.TileSize * x;
                    int tileY = Config.TileSize * y;

                    int backgrond = floorScanner.nextInt();
                    int solid = solidScanner.nextInt();

                    if (backgrond != -1) {
                        backgrondMap[y][x] = new Block(tileX, tileY, Config.TileSize, Config.TileSize, Assets.TILEMAP[backgrond], DepthType.MAP, false);
                    }
                    if (solid != -1) {
                        if (y < 11) {
                            Table table = new Table(tileX, tileY, Assets.DISHMAP[0]);
                            if (y == 6) table.addFood(Assets.FOODLIST.get(random.nextInt(3)).clone());
                            objectMap[y][x] = table;
                        } else {
                            objectMap[y][x] = new Block(tileX, tileY, Config.TileSize, Config.TileSize, Assets.TILEMAP[solid], DepthType.OBJECT, true);
                        }
                    }
                }
            }

            for (int x = 1; x < 4; x++) {
                FoodBox foodBox = new FoodBox(Config.TileSize * x, Config.TileSize * 5, Assets.sodwkdrh, Assets.FOODLIST.get(x - 1).clone());
                objectMap[5][x] = foodBox;
            }

            objectMap[7][8] = new Knife(Config.TileSize * 8, Config.TileSize * 7, Assets.knife);

        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void updateData(KeyEventData keyEventData) {
        // 키 입력 전송
        if (keyEventData.w()) player.setMoveY(-1);
        if (keyEventData.a()) player.setMoveX(-1);
        if (keyEventData.s()) player.setMoveY(1);
        if (keyEventData.d()) player.setMoveX(1);
        if (keyEventData.space()) player.onSpace();

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
        }

        player.updateAnimation();
        player.controlMessageBox();

        FoodType foodType = null;
        if(player.peekFood() != null){
            foodType= player.peekFood().getFoodType();
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
                        if (obj.getProgressValue() > 0) {
                            cookTogether.addRenderData(obj.getProgressRenderData());
                        }
                        if (obj.isTouch()) cookTogether.addRenderData(obj.getTouchRenderData());
                        if (obj.isHoldFood()) {
                            for (int i = 0; i < obj.getHoldFoodIndex(); i++) {
                                cookTogether.addRenderData(obj.getFoodRenderData(i));
                                cookTogether.addRenderData(obj.peekFood().getStringRenderData()); // TODO: 추후 제거
                            }
                        }
                    } else {
                        cookTogether.addRenderData(object.getImageRenderData());
                    }
                }
            }
        }
    }

    public void recvBlockPacket(BlockPacket blockPacket) {
        switch (blockPacket.blockType) {
            case Table -> ((Table) objectMap[blockPacket.getY()][blockPacket.getX()]).setEventData(blockPacket);
            case FoodBox -> ((FoodBox) objectMap[blockPacket.getY()][blockPacket.getX()]).setEventData(blockPacket);
            case Knife -> ((Knife) objectMap[blockPacket.getY()][blockPacket.getX()]).setEventData(blockPacket);
        }
    }

    public void actionBlockInMap(int y, int x) {
        if (objectMap[y][x] instanceof FoodBox foodBox) {
            foodBox.action();
        } else if (objectMap[y][x] instanceof Knife knife) {
            knife.action();
        } else {
            System.out.println("꺅 에러다.");
        }
    }
}
