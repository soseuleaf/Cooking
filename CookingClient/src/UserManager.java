import Data.EventPacket;
import Data.Player;
import Data.RenderData;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final CookTogether cookTogether;
    private final Map<String, Player> playerMap = new HashMap<>();

    public UserManager(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
    }

    public void recvEventPacket(EventPacket eventPacket){
        if(playerMap.containsKey(eventPacket.UserName)){
            Player player = playerMap.get(eventPacket.UserName);
            player.setEvent(eventPacket);
            playerMap.put(eventPacket.UserName, player);
        }
        else{
            addNewUser(eventPacket.UserName, new Player(eventPacket.UserName, eventPacket.x, eventPacket.y));
        }
    }

    public void addNewUser(String name, Player player) {
        playerMap.put(name, player);
    }

    public void update(){
        for (String key : playerMap.keySet()) {
            Player user = playerMap.get(key);
            cookTogether.addRenderData(new RenderData(user.getX(), user.getY(), user.getSprite()));
        }
    }
}
