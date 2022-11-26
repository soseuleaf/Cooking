package Component.Packet;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class ConnectPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = -1662022300670224623L;
    private final UUID uuid;
    private final int code; // 100: connect, 200: disconnect
    private final String name;
    private final int x;
    private final int y;

    public ConnectPacket(UUID uuid, int code, String name, int x, int y) {
        this.uuid = uuid;
        this.code = code;
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
