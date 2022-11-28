package Component.Packet;

import Component.Type.StateType;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class StatePacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final StateType stateType;
    private final double time;
    private final int score;

    public StatePacket(StateType stateType, double time, int score) {
        this.stateType = stateType;
        this.time = time;
        this.score = score;
    }
}
