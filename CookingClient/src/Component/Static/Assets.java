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
    public static BufferedImage[] NUMBER = new BufferedImage[11];
    public static BufferedImage[] TILEMAP = new BufferedImage[512];
    public static BufferedImage[] DISHMAP = new BufferedImage[24];
    public static BufferedImage BLACKTILE = new BufferedImage(16, 16, 1);
    public static BufferedImage[] refrig = new BufferedImage[2];
    public static BufferedImage[] pot = new BufferedImage[2];
    public static BufferedImage[] fryer = new BufferedImage[4];
    public static BufferedImage[] frypan = new BufferedImage[2];
    public static BufferedImage knife;
    public static BufferedImage trash;
    public static BufferedImage order;
    public static BufferedImage orderTime;
    public static BufferedImage actionIcon;
    public static BufferedImage foodout;
    public static BufferedImage touchBlock;
    public static BufferedImage progressBlock;
    public static ArrayList<Food> FOODLIST = new ArrayList<>();

    public static void init() {
        loadCharacter();
        loadTileMap();
        loadNumber();

        refrig[0] = AssetLoader.loadImage("/textures/single/refrig_1.png");
        refrig[1] = AssetLoader.loadImage("/textures/single/refrig_2.png");
        pot[0] = AssetLoader.loadImage("/textures/single/pot_1.png");
        pot[1] = AssetLoader.loadImage("/textures/single/pot_2.png");
        fryer[0] = AssetLoader.loadImage("/textures/single/fryer_left_1.png");
        fryer[1] = AssetLoader.loadImage("/textures/single/fryer_right_1.png");
        fryer[2] = AssetLoader.loadImage("/textures/single/fryer_left_2.png");
        fryer[3] = AssetLoader.loadImage("/textures/single/fryer_right_2.png");
        frypan[0] = AssetLoader.loadImage("/textures/single/frypan_1.png");
        frypan[1] = AssetLoader.loadImage("/textures/single/frypan_2.png");
        trash = AssetLoader.loadImage("/textures/single/trash.png");
        knife = AssetLoader.loadImage("/textures/single/knife.png");
        order = AssetLoader.loadImage("/textures/single/order.png");
        orderTime = AssetLoader.loadImage("/textures/single/order_time.png");
        actionIcon = AssetLoader.loadImage("/textures/single/action.png");
        foodout = AssetLoader.loadImage("/textures/single/foodout.png");
        touchBlock = AssetLoader.loadImage("/textures/single/touch_block.png");
        progressBlock = AssetLoader.loadImage("/textures/single/progress_block.png");

        // 음식 관련
        loadFood();
        initFood();
    }

    private static void initFood() {
        for (FoodType type : FoodType.values()) {
            FOODLIST.add(new Food(type));
        }
    }

    private static void loadNumber() {
        SpriteSheet playerSprites = new SpriteSheet(AssetLoader.loadImage("/textures/number.png"));
        spriteHelper(NUMBER, playerSprites, 0, 0, Config.TileSpriteSize, Config.TileSpriteSize, false);
    }

    private static void loadFood() {
        SpriteSheet dishSprite = new SpriteSheet(AssetLoader.loadImage(Config.DISHMAP));
        for (int i = 0, y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++, i++) {
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
        for (int y = 0; y < 3; y++) {
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
