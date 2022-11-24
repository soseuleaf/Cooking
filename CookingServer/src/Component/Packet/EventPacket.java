package Component.Packet;

import Component.Type.FoodType;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class EventPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int code; // 10: Send order, 20: plz Check Food
    private final FoodType foodType;

    public EventPacket(int code, FoodType foodType) {
        this.code = code;
        this.foodType = foodType;
    }
}
