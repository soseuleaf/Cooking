package Data;

import Render.Animation;
import Render.Assets;

import java.awt.image.BufferedImage;

public class Tile extends GameObject {
    public Tile(int x, int y) {
        super(x, y);
        this.depth = RenderDepth.MAP;
        this.currentAnimation = new Animation(99, Assets.BlankTile);
    }

    @Override
    public BufferedImage getSprite() {
        return null;
    }
}
