package Component.Packet;

import Component.Type.FoodType;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class UserPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID uuid;
    private final int x;
    private final int y;
    private final FoodType foodType;

    public UserPacket(UUID uuid, int x, int y, FoodType foodType) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.foodType = foodType;
    }
}