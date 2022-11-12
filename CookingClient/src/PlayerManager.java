import Data.*;
import Data.KeyEventData;
import Data.RenderDepth;

public class PlayerManager {
    private final CookTogether cookTogether;
    private final Player player;

    public PlayerManager(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
        this.player = new Player();
    }

    public void updateData(KeyEventData keyEventData) {
        // 키 입력 전송
        if (keyEventData == null) return;
        if (keyEventData.w()) player.setMoveY(-1);
        if (keyEventData.a()) player.setMoveX(-1);
        if (keyEventData.s()) player.setMoveY(1);
        if (keyEventData.d()) player.setMoveX(1);

        // 플레이더 데이터 업데이트
        player.updateMove();
        player.updateAnimation();
        player.setMoveZero();

        // 데이터 전송
        cookTogether.sendEventPacket(player.getX(), player.getY());
    }

    public void updateRender() {
        cookTogether.addRenderData(new ImageRenderData(player.getX(), player.getY(), player.getSprite(), RenderDepth.OBJECT));
        cookTogether.addRenderData(new StringRenderData(player.getX(), player.getY(), player.getName()));
    }
}
