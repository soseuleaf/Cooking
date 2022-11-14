import Data.*;
import Render.AssetLoader;
import Render.Assets;

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
    private int playerTileX = 1;
    private int playerTileY = 1;

    public PlayManager(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
        this.player = new Player();
        initMap();
    }

    private void initMap() {
        loadWorld();
    }

    private void loadWorld() {
        Scanner floorScanner;
        Scanner solidScanner;

        try {
            floorScanner = new Scanner(AssetLoader.loadText(Config.BACKGROUNDMAP));
            solidScanner = new Scanner(AssetLoader.loadText(Config.SOLIDMAP));

            for (int y = 0; y < Config.MAP_Y; y++) {
                for (int x = 0; x < Config.MAP_X; x++) {
                    int tileX = Config.TileSize * x;
                    int tileY = Config.TileSize * y;

                    int backgrond = floorScanner.nextInt();
                    int solid = solidScanner.nextInt();

                    if (backgrond != -1) {
                        backgrondMap[y][x] = new Block(tileX, tileY, Config.TileSize, Config.TileSize, Assets.TILEMAP[backgrond], RenderDepth.MAP, false, true);
                    }
                    if (solid != -1) {
                        if (y < 11) {
                            objectMap[y][x] = new InteractionBlock(tileX, tileY, Config.TileSize, Config.TileSize, Assets.TILEMAP[solid]);
                            objectMap[y][x].addFood(Assets.FOODLIST.get(0).clone());
                        } else {
                            objectMap[y][x] = new Block(tileX, tileY, Config.TileSize, Config.TileSize, Assets.TILEMAP[solid], RenderDepth.OBJECT, true, false);
                        }
                    }
                }
            }
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
        if (keyEventData.space()) player.onSpace(true);

        // 충격의 충돌 처리 관련 계산
        playerTileX = player.getTileX();
        playerTileY = player.getTileY();

        if (playerTileY < 0) playerTileY = 0;
        if (playerTileY > Config.MAP_Y) playerTileY = Config.MAP_Y;
        if (playerTileX < 0) playerTileX = 0;
        if (playerTileX > Config.MAP_X) playerTileX = Config.MAP_X;

        aroundObject[0] = objectMap[playerTileY - 1][playerTileX - 1];
        aroundObject[1] = objectMap[playerTileY - 1][playerTileX];
        aroundObject[2] = objectMap[playerTileY - 1][playerTileX + 1];
        aroundObject[3] = objectMap[playerTileY][playerTileX - 1];
        aroundObject[4] = objectMap[playerTileY][playerTileX];
        aroundObject[5] = objectMap[playerTileY][playerTileX + 1];
        aroundObject[6] = objectMap[playerTileY + 1][playerTileX - 1];
        aroundObject[7] = objectMap[playerTileY + 1][playerTileX];
        aroundObject[8] = objectMap[playerTileY + 1][playerTileX + 1];

        // 플레이어 데이터 업데이트
        switch (player.updateData(aroundObject)) { // 아무튼 스페이스바 클릭으로 이벤트가 발생했다면
            case FOOD_PUT ->
                    cookTogether.sendFoodPacket(EventEnum.FOOD_PUT, player.collisionBlockTileY(), player.collisionBlockTileX());
            case FOOD_DOWN ->
                    cookTogether.sendFoodPacket(EventEnum.FOOD_DOWN, player.collisionBlockTileY(), player.collisionBlockTileX());
        }
        player.updateAnimation();
        player.controlMessageBox();

        // 데이터 전송
        cookTogether.sendEventPacket(player.getX(), player.getY());

        // 데이터 정리
        player.setMoveX(0);
        player.setMoveY(0);
    }

    public void updateRender() {
        cookTogether.addRenderData(player.getImageRenderData());
        cookTogether.addRenderData(player.getStringRenderData());

        if (player.isHoldFood()) {
            cookTogether.addRenderData(player.peekFood().getImageRenderData());
        }
        if (player.canMessageRender()) {
            cookTogether.addRenderData(player.getMessageRenderData());
        }

        for (int y = 0; y < Config.MAP_Y; y++) {
            for (int x = 0; x < Config.MAP_X; x++) {
                Block background = backgrondMap[y][x];
                Block object = objectMap[y][x];

                if (background != null) {
                    cookTogether.addRenderData(background.getImageRenderData());
                }

                if (object != null) {
                    if (object instanceof InteractionBlock obj) {
                        cookTogether.addRenderData(obj.getImageRenderData());
                        if (obj.isTouch()) {
                            cookTogether.addRenderData(obj.getTouchRenderData());
                        }
                        if (obj.isHoldFood()) {
                            cookTogether.addRenderData(obj.peekFood().getImageRenderData());
                            cookTogether.addRenderData(obj.peekFood().getStringRenderData());
                        }
                    } else {
                        cookTogether.addRenderData(object.getImageRenderData());
                    }
                }
            }
        }
    }

    public void addFoodInMap(int y, int x, Food food) {
        objectMap[y][x].addFood(food);
    }

    public Food popFoodInMap(int y, int x) {
        return objectMap[y][x].popFood();
    }
}
