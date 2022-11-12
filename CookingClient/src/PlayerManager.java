import Data.KeyEventData;
import Data.Player;
import Data.RenderData;

public class PlayerManager {
    private final CookTogether cookTogether;
    private final Player player;
    private KeyEventData keyEventData;

    public PlayerManager(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
        this.player = new Player();
    }

    public void update() {
        if (keyEventData == null) return;
        if (keyEventData.w()) player.moveY(-1);
        if (keyEventData.a()) player.moveX(-1);
        if (keyEventData.s()) player.moveY(1);
        if (keyEventData.d()) player.moveX(1);
        cookTogether.addRenderData(new RenderData(player.getX(), player.getY(), player.getSprite()));
        cookTogether.sendEventPacket(player.getX(), player.getY());
    }

    public void setKeyEventData(KeyEventData keyEventData) {
        this.keyEventData = keyEventData;
    }
}
