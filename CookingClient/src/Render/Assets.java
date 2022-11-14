package Render;

import Data.Config;

import java.awt.image.BufferedImage;

public class Assets {
    public static BufferedImage[] CHARACTER_IDLE_LEFT = new BufferedImage[1];
    public static BufferedImage[] CHARACTER_IDLE_RIGHT = new BufferedImage[1];
    public static BufferedImage[] CHARACTER_MOVE_LEFT = new BufferedImage[8];
    public static BufferedImage[] CHARACTER_MOVE_RIGHT = new BufferedImage[8];
    public static BufferedImage[] TILEMAP = new BufferedImage[512];
    public static BufferedImage[] TESTMAP = new BufferedImage[512];
    public static BufferedImage BLACKTILE = new BufferedImage(16, 16, 1);
    public static BufferedImage TEST;

    public static void init() {
        loadCharacter();
        loadTest();
        loadTileMap();
    }

    private static void loadTest() {
        SpriteSheet testSprite = new SpriteSheet(ImageLoader.loadImage("/textures/char_kirby.png"));
        TEST = testSprite.crop(10 * Config.CharacterSpriteSize, 12 * Config.CharacterSpriteSize, Config.CharacterSpriteSize, Config.CharacterSpriteSize).build();
    }

    private static void loadCharacter() {
        SpriteSheet playerSprites = new SpriteSheet(ImageLoader.loadImage("/textures/char_kirby.png"));
        spriteHelper(CHARACTER_IDLE_LEFT, playerSprites, 0, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(CHARACTER_IDLE_RIGHT, playerSprites, 0, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
        spriteHelper(CHARACTER_MOVE_LEFT, playerSprites, 1, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(CHARACTER_MOVE_RIGHT, playerSprites, 1, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
    }

    private static void loadTileMap() {
        SpriteSheet tiles = new SpriteSheet(ImageLoader.loadImage("/textures/kitchen_tile.png"));
        int i = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                TILEMAP[i++] = tiles.crop(x * Config.TileSpriteSize, y * Config.TileSpriteSize, Config.TileSpriteSize, Config.TileSpriteSize).build();
            }
        }
    }

    private static void spriteHelper(BufferedImage[] bufferedImages, SpriteSheet s, int x, int y, int width, int height, boolean flip) {
        for (int i = 0; i < bufferedImages.length; i++, x++) {
            s.crop(x * width, y * height, width, height);
            if (flip) {
                s.flipX();
            }
            bufferedImages[i] = s.build();
        }
    }
}
