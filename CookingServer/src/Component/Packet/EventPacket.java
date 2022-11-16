package Component.Packet;

import Component.Type.EventType;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class EventPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public EventType code;
    public UUID uuid;
    public String data;
    public int x, y;

    public EventPacket(UUID uuid, EventType code, String msg, int x, int y) {
        this.uuid = uuid;
        this.code = code;
        this.data = msg;
        this.x = x;
        this.y = y;
    }
}