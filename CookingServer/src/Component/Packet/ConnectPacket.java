package Component.Packet;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class ConnectPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID uuid;
    private final int code; // 100: connect, 150: loginPacket, 200: disconnect
    private final int index; // 100: connect, 150: loginPacket, 200: disconnect
    private final String name;
    private final int x;
    private final int y;

    public ConnectPacket(int code, UUID uuid, int index, String name, int x, int y) {
        this.code = code;
        this.uuid = uuid;
        this.index = index;
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
