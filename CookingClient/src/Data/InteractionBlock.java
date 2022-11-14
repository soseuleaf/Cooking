package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public class InteractionBlock extends Block {
    private boolean touch = false;
    private boolean canUse = true;
    private final BufferedImage touchSprite;
    private final Animation interaction;

    public InteractionBlock(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite, RenderDepth.OBJECT, true, true);
        this.touchSprite = Assets.BLACKTILE;
        this.interaction = new Animation(99, Assets.TILEMAP[2]);
    }

    public void setTouch(boolean touch) {
        this.touch = touch;
    }

    public boolean isTouch() {
        return touch;
    }

    public RenderData getTouchRenderData() {
        BufferedImage outSprite = Assets.TILEMAP[20];
        setTouch(false);
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), outSprite, RenderDepth.EFFECT);
    }

    @Override
    public RenderData getImageRenderData() {
        BufferedImage outSprite;
        if (!canUse) {
            outSprite = interaction.getCurrentFrame();
        } else {
            outSprite = getSprite();
        }
        return new ImageRenderData(x + Config.TileSize - getWidth(), y + Config.TileSize - getHeight(), getWidth(), getHeight(), outSprite, RenderDepth.OBJECT);
    }
}
