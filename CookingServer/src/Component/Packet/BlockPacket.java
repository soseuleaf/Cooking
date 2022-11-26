package Component.Packet;

import Component.Type.BlockType;
import Component.Type.FoodType;
import Component.Type.WorkState;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class BlockPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final BlockType blockType;
    private final int x;
    private final int y;
    private final FoodType[] foodType;
    private final WorkState workState;
    private final double progress;

    public BlockPacket(BlockType blockType, int y, int x, FoodType[] foodType, WorkState workState, double progress) {
        this.blockType = blockType;
        this.y = y;
        this.x = x;
        this.foodType = foodType;
        this.workState = workState;
        this.progress = progress;
    }
}