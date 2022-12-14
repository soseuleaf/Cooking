package Component.Packet;

import Component.Type.StateType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class StatePacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final StateType stateType;
    private final double time;
    private final int successFood;
    private final int failedFood;
    private final int averageTime;
    private final int score;
    @Setter
    private boolean watch;

    public StatePacket(StateType stateType, double time, int successFood, int failedFood, int averageTime, int score, boolean watch) {
        this.stateType = stateType;
        this.time = time;
        this.successFood = successFood;
        this.failedFood = failedFood;
        this.averageTime = averageTime;
        this.score = score;
        this.watch = watch;
    }
}
