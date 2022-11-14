import Data.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {
    private final CookTogether cookTogether;
    private final Map<UUID, User> userMap = new HashMap<>();

    public UserManager(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
    }

    public void recvEventPacket(EventPacket eventPacket) {
        if (userMap.containsKey(eventPacket.uuid)) {
            updateUser(userMap.get(eventPacket.uuid), eventPacket);
        } else {
            addNewUser(eventPacket.uuid, new User(eventPacket.x, eventPacket.y, "닉네임임시"));
        }
    }

    private void updateUser(User user, EventPacket eventPacket) {
        user.bindEvent(eventPacket);
        userMap.put(eventPacket.uuid, user);
    }

    private void addNewUser(UUID uuid, User user) {
        userMap.put(uuid, user);
    }

    public void updateRender() {
        for (UUID uuid : userMap.keySet()) {
            User user = userMap.get(uuid);
            cookTogether.addRenderData(user.getImageRenderData());
            cookTogether.addRenderData(user.getStringRenderData());
        }
    }
}
