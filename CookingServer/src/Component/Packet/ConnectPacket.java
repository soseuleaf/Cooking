package Component.Packet;
import lombok.Getter;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class ConnectPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID uuid;
    private int code; // 100: connect, 200: disconnect
    private String name;
    private int x, y;

    public ConnectPacket(UUID uuid, int code, String name, int x, int y) {
        this.uuid = uuid;
        this.code = code;
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
