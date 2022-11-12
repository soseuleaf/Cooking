package Data;

public class StringRenderData extends RenderData {
    private final String message;

    public StringRenderData(int x, int y, String message) {
        super(x, y, RenderDepth.UI);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
