package Data;

import Render.Assets;

import java.awt.image.BufferedImage;

public abstract class Object {
    // 렌더링 좌표
    protected int x;
    protected int y;
    private RenderDepth depth = RenderDepth.OBJECT;

    // 이미지 사이즈
    private int width = Config.TileSize;
    private int height = Config.TileSize;

    // 기본 스프라이트
    private BufferedImage sprite = Assets.BLACKTILE;


    public Object(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected void setDepth(RenderDepth depth) {
        this.depth = depth;
    }

    protected void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public RenderDepth getDepth() {
        return depth;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public abstract RenderData getImageRenderData();
}
