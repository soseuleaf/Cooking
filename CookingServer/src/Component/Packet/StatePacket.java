package Component.Packet;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class StatePacket implements Serializable {
    @Serial
    private static final long serialVersionUID = -1662022300670224623L;
    private final double time;
    private final int score;

    public StatePacket(double time, int score) {
        this.time = time;
        this.score = score;
    }
}
