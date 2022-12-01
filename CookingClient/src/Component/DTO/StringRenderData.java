package Component.DTO;

import Component.Type.DepthType;
import lombok.Getter;

@Getter
public class StringRenderData extends RenderData {

    private final String message;
    private final int fontSize;

    public StringRenderData(int x, int y, String message, int fontSize) {
        super(x, y, DepthType.UI);
        this.message = message;
        this.fontSize = fontSize;
    }
}
