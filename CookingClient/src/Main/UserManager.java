package Main;

import Component.*;
import Component.Packet.EventPacket;

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
    }

    private void addNewUser(UUID uuid, User user) {
        userMap.put(uuid, user);
    }

    public void updateRender() {
        for (UUID uuid : userMap.keySet()) {
            User user = userMap.get(uuid);
            cookTogether.addRenderData(user.getImageRenderData());
            cookTogether.addRenderData(user.getStringRenderData());

            if (user.isHold()) {
                Food food = user.peekFood();
                food.setParentPosition(user.getX(), user.getY(), true);
                cookTogether.addRenderData(food.getImageRenderData());
            }
        }
    }

    public void addFoodByUser(UUID uuid, Food food) {
        if (userMap.containsKey(uuid)) {
            userMap.get(uuid).addFood(food);
        }
    }

    public Food popFoodByUser(UUID uuid) {
        return userMap.get(uuid).popFood();
    }
}
