package Data;

import java.io.Serial;
import java.io.Serializable;

public class EventPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public EventEnum code;
    public String UserName;
    public String data;
    public int x, y;

    public EventPacket(EventEnum code, String UserName, String msg, int x, int y) {
        this.code = code;
        this.UserName = UserName;
        this.data = msg;
        this.x = x;
        this.y = y;
    }
}