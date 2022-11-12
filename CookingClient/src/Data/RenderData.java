package Data;

import java.awt.image.BufferedImage;

public record RenderData(int x, int y, BufferedImage sprite) {
    public String toString(){
        return "x: " + x + "y: " + y;
    }
}
