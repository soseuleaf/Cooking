package Component.Packet;

import Component.Type.EventType;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class FoodPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public EventType code;
    public UUID uuid;
    public int x, y;

    public FoodPacket(UUID uuid, EventType code, int x, int y) {
        this.uuid = uuid;
        this.code = code;
        this.x = x;
        this.y = y;
    }
}