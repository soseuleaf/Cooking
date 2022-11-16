package Component.Static;

import Component.Food;
import Component.Type.FoodType;
import Component.Render.AssetLoader;
import Component.Render.SpriteSheet;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Assets {
    public static BufferedImage[] CHARACTER_IDLE_LEFT = new BufferedImage[1];
    public static BufferedImage[] CHARACTER_IDLE_RIGHT = new BufferedImage[1];
    public static BufferedImage[] CHARACTER_MOVE_LEFT = new BufferedImage[8];
    public static BufferedImage[] CHARACTER_MOVE_RIGHT = new BufferedImage[8];
    public static BufferedImage[] TILEMAP = new BufferedImage[512];
    public static BufferedImage[] DISHMAP = new BufferedImage[512];
    public static BufferedImage BLACKTILE = new BufferedImage(16, 16, 1);
    public static BufferedImage sodwkdrh;
    public static ArrayList<Food> FOODLIST = new ArrayList<>();

    public static void init() {
        loadCharacter();
        loadTileMap();

        sodwkdrh = AssetLoader.loadImage("/textures/sodwkdrh.png");

        // 음식 관련
        loadFood();
        initFood();
    }

    private static void initFood() {
        for (FoodType type : FoodType.values()) {
            FOODLIST.add(new Food(type));
        }
    }

    private static void loadFood() {
        SpriteSheet dishSprite = new SpriteSheet(AssetLoader.loadImage(Config.DISHMAP));
        for (int i = 0, y = 0; y < 11; y++) {
            for (int x = 0; x < 10; x++, i++) {
                DISHMAP[i] = dishSprite.crop(x * Config.TileSpriteSize, y * Config.TileSpriteSize, Config.TileSpriteSize, Config.TileSpriteSize).build();
            }
        }

    }

    private static void loadCharacter() {
        SpriteSheet playerSprites = new SpriteSheet(AssetLoader.loadImage(Config.CHARACTERSPRITE));
        spriteHelper(CHARACTER_IDLE_LEFT, playerSprites, 0, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(CHARACTER_IDLE_RIGHT, playerSprites, 0, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
        spriteHelper(CHARACTER_MOVE_LEFT, playerSprites, 1, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(CHARACTER_MOVE_RIGHT, playerSprites, 1, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
    }

    private static void loadTileMap() {
        SpriteSheet tiles = new SpriteSheet(AssetLoader.loadImage(Config.TILEMAP));
        int i = 0;
        for (int y = 0; y < 5; y++) {
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
