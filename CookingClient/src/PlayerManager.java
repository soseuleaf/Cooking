import Data.GameObject;
import Data.*;

public class PlayerManager {
    private final CookTogether cookTogether;
    private final Player player;
    private GameObject[] aroundObject = new GameObject[9];

    private GameObject[][] map = new GameObject[12][12];
    private int playerTileX = 1;
    private int playerTileY = 1;

    public PlayerManager(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
        this.player = new Player();
        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 12; x++) {
                int tileX = Config.TileSize * x - Config.TileSize;
                int tileY = Config.TileSize * y - Config.TileSize;
                if (x == 3 && y == 3) {
                    map[y][x] = new InteractionObject(tileX, tileY);
                } else if (x == 0 || y == 0 || x == 11 || y == 11) {
                    map[y][x] = new InteractionObject(tileX, tileY);
                } else {
                    map[y][x] = new Tile(tileX, tileY);
                }
            }
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
        playerTileX = (player.getX() + (Config.CharacterSize / 2)) / Config.CharacterSize + 1;
        playerTileY = (player.getY() + (Config.CharacterSize / 2)) / Config.CharacterSize + 1;

        if (playerTileY < 1) playerTileY = 1;
        if (playerTileY > 10) playerTileY = 10;
        if (playerTileX < 1) playerTileX = 1;
        if (playerTileX > 10) playerTileX = 10;

        aroundObject[0] = map[playerTileY - 1][playerTileX - 1];
        aroundObject[1] = map[playerTileY - 1][playerTileX];
        aroundObject[2] = map[playerTileY - 1][playerTileX + 1];
        aroundObject[3] = map[playerTileY][playerTileX - 1];
        aroundObject[4] = map[playerTileY][playerTileX];
        aroundObject[5] = map[playerTileY][playerTileX + 1];
        aroundObject[6] = map[playerTileY + 1][playerTileX - 1];
        aroundObject[7] = map[playerTileY + 1][playerTileX];
        aroundObject[8] = map[playerTileY + 1][playerTileX + 1];

        // 플레이더 데이터 업데이트
        player.updateMove(aroundObject);

        // 데이터 전송
        cookTogether.sendEventPacket(player.getX(), player.getY());
    }

    public void updateRender() {
        cookTogether.addRenderData(new ImageRenderData(player.getX(), player.getY(), player.getSprite(), RenderDepth.OBJECT));
        cookTogether.addRenderData(new StringRenderData(player.getX(), player.getY(), player.getName()));

        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 12; x++) {
                GameObject object = map[y][x];
                cookTogether.addRenderData(new ImageRenderData(object.getX(), object.getY(), object.getSprite(), object.getDepth()));
            }
        }
    }
}
