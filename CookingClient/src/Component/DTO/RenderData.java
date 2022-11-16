package Component.DTO;

import Component.Type.DepthType;

public class RenderData implements Comparable<RenderData> {
    private final int x;
    private final int y;
    private final int z;

    public RenderData(int x, int y, DepthType z) {
        this.x = x;
        this.y = y;
        this.z = z.ordinal();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int compareTo(RenderData o) {
        if (getZ() == o.getZ()) {
            return getY() - o.getY();
        } else {
            return getZ() - o.getZ();
        }
    }
}

