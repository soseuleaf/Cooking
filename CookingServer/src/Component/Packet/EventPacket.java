package Component.Packet;

import Component.Type.FoodType;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class EventPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int code; // 10: Send order, 20: plz Check Food, 30 : success, 40 : failed
    private final UUID orderUuid;
    private final FoodType foodType;
    private final int max;

    public EventPacket(int code, UUID orderUuid, FoodType foodType, int max) {
        this.code = code;
        this.orderUuid = orderUuid;
        this.foodType = foodType;
        this.max = max;
    }
}
