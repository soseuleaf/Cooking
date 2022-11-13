import Data.Object;
import Data.*;
import Render.Assets;

import java.util.Objects;
import java.util.Scanner;

public class PlayerManager {
    private final CookTogether cookTogether;
    private final Player player;
    private final Object[] aroundObject = new Object[9];

    private final Object[][] floorMap = new Object[Config.MAP_Y][Config.MAP_X];
    private final Object[][] tileMap = new Object[Config.MAP_Y][Config.MAP_X];
    private final Object[][] objectMap = new Object[Config.MAP_Y][Config.MAP_X];
    private int playerTileX = 1;
    private int playerTileY = 1;

    public PlayerManager(CookTogether cookTogether) {
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
        Scanner nonSolidScanner;

        try {
            floorScanner = new Scanner(Objects.requireNonNull(CookTogether.class.getResourceAsStream(Config.FLOORMAP)));
            solidScanner = new Scanner(Objects.requireNonNull(CookTogether.class.getResourceAsStream(Config.SOLIDMAP)));
            nonSolidScanner = new Scanner(Objects.requireNonNull(CookTogether.class.getResourceAsStream(Config.NONESOLIDMAP)));

            for (int y = 0; y < Config.MAP_Y; y++) {
                for (int x = 0; x < Config.MAP_X; x++) {
                    int tileX = Config.TileSize * x;
                    int tileY = Config.TileSize * y;

                    int floor = floorScanner.nextInt();
                    int object = solidScanner.nextInt();
                    int tile = nonSolidScanner.nextInt();

                    if (floor != -1) {
                        floorMap[y][x] = new Object(tileX, tileY, Assets.TILEMAP[floor], RenderDepth.FLOOR, false);
                    }
                    if (tile != -1) {
                        tileMap[y][x] = new Object(tileX, tileY, Assets.TILEMAP[tile], RenderDepth.FLOOR, false);
                    }
                    if (object != -1) {
                        objectMap[y][x] = new InteractionObject(tileX, tileY, Assets.TILEMAP[object], Assets.BLACKTILE);
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
        if (keyEventData == null) return;
        if (keyEventData.w()) player.setMoveY(-1);
        if (keyEventData.a()) player.setMoveX(-1);
        if (keyEventData.s()) player.setMoveY(1);
        if (keyEventData.d()) player.setMoveX(1);

        // 충격의 충돌 처리 관련 계산
        playerTileX = (player.getX() + (Config.CharacterSize / 2)) / Config.CharacterSize;
        playerTileY = (player.getY() + (Config.CharacterSize / 2)) / Config.CharacterSize;

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

        // 플레이더 데이터 업데이트
        player.updateMove(aroundObject);

        // 데이터 전송
        cookTogether.sendEventPacket(player.getX(), player.getY());
    }

    public void updateRender() {
        cookTogether.addRenderData(new ImageRenderData(player.getX(), player.getY(), player.getSprite(), RenderDepth.OBJECT));
        cookTogether.addRenderData(new StringRenderData(player.getX(), player.getY(), player.getName()));

        for (int y = 0; y < Config.MAP_Y; y++) {
            for (int x = 0; x < Config.MAP_X; x++) {
                Object floor = floorMap[y][x];
                Object tile = tileMap[y][x];
                Object object = objectMap[y][x];

                if (floor != null) {
                    cookTogether.addRenderData(new ImageRenderData(floor.getX(), floor.getY(), floor.getSprite(), floor.getDepth()));
                }

                if (tile != null) {
                    cookTogether.addRenderData(new ImageRenderData(tile.getX(), tile.getY(), tile.getSprite(), tile.getDepth()));
                }

                if (object != null) {
                    cookTogether.addRenderData(new ImageRenderData(object.getX(), object.getY(), object.getSprite(), object.getDepth()));
                }
            }
        }
    }
}
