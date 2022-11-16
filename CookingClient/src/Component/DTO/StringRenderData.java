package Component.DTO;

import Component.Type.DepthType;

public class StringRenderData extends RenderData {
    private final String message;

    public StringRenderData(int x, int y, String message) {
        super(x, y, DepthType.UI);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
