package Main;

import Component.*;
import Component.Packet.ConnectPacket;
import Component.Packet.UserPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {
    private final CookTogether cookTogether;
    private final Map<UUID, User> userMap = new HashMap<>();

    public UserManager(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
    }

    public void recvConnectPacket(ConnectPacket connectPacket) {
        switch (connectPacket.getCode()) {
            case 150 ->
                    addNewUser(connectPacket.getUuid(), new User(connectPacket.getX(), connectPacket.getY(), connectPacket.getUuid().toString()));
            case 200 -> removeUser(connectPacket.getUuid());
            default -> System.out.println("ConnectPacketError");
        }
    }

    public void recvUserPacket(UserPacket userPacket) {
        if (userMap.containsKey(userPacket.getUuid())) {
            userMap.get(userPacket.getUuid()).bindEvent(userPacket);
        } else {
            addNewUser(userPacket.getUuid(), new User(userPacket.getX(), userPacket.getY(), "커넥트 패킷 오류"));
        }
    }

    private void addNewUser(UUID uuid, User user) {
        userMap.put(uuid, user);
    }

    private void removeUser(UUID uuid) {
        userMap.remove(uuid);
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


}
