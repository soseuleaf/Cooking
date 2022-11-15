package Data;

import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
public abstract class Object {
    // 렌더링 좌표
    protected int x;
    protected int y;
    private final RenderDepth depth;

    // 이미지 사이즈
    private final int width;
    private final int height;

    // 기본 스프라이트
    private final BufferedImage sprite;

    public Object(int x, int y, int width, int height, BufferedImage sprite, RenderDepth depth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.depth = depth;
    }

    public int getTileX() {
        return (x + Config.TileSize / 2) / Config.TileSize;
    }

    public int getTileY() {
        return (y + Config.TileSize / 2) / Config.TileSize;
    }

    public abstract RenderData getImageRenderData();
}
