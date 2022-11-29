package Component.Static;

import Component.Food;
import Component.Render.AssetLoader;
import Component.Render.SpriteSheet;
import Component.Type.FoodType;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Assets {

    // Player1
    public static BufferedImage[] PLAYER1_IDLE_LEFT = new BufferedImage[1];
    public static BufferedImage[] PLAYER1_IDLE_RIGHT = new BufferedImage[1];
    public static BufferedImage[] PLAYER1_MOVE_LEFT = new BufferedImage[8];
    public static BufferedImage[] PLAYER1_MOVE_RIGHT = new BufferedImage[8];

    // Player2
    public static BufferedImage[] PLAYER2_IDLE_LEFT = new BufferedImage[1];
    public static BufferedImage[] PLAYER2_IDLE_RIGHT = new BufferedImage[1];
    public static BufferedImage[] PLAYER2_MOVE_LEFT = new BufferedImage[8];
    public static BufferedImage[] PLAYER2_MOVE_RIGHT = new BufferedImage[8];

    // Player3
    public static BufferedImage[] PLAYER3_IDLE_LEFT = new BufferedImage[1];
    public static BufferedImage[] PLAYER3_IDLE_RIGHT = new BufferedImage[1];
    public static BufferedImage[] PLAYER3_MOVE_LEFT = new BufferedImage[8];
    public static BufferedImage[] PLAYER3_MOVE_RIGHT = new BufferedImage[8];

    // Player4
    public static BufferedImage[] PLAYER4_IDLE_LEFT = new BufferedImage[1];
    public static BufferedImage[] PLAYER4_IDLE_RIGHT = new BufferedImage[1];
    public static BufferedImage[] PLAYER4_MOVE_LEFT = new BufferedImage[8];
    public static BufferedImage[] PLAYER4_MOVE_RIGHT = new BufferedImage[8];


    public static BufferedImage[] NUMBER = new BufferedImage[11];
    public static BufferedImage[] TILEMAP = new BufferedImage[512];
    public static BufferedImage[] WAITROOM_TILEMAP = new BufferedImage[512];
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
    public static BufferedImage isMeArrow;
    public static BufferedImage manual;
    public static BufferedImage title;
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
        isMeArrow = AssetLoader.loadImage("/textures/single/isme_arrow.png");
        manual = AssetLoader.loadImage("/textures/single/manual.png");

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
        spriteHelper(PLAYER1_IDLE_LEFT, playerSprites, 0, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(PLAYER1_IDLE_RIGHT, playerSprites, 0, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
        spriteHelper(PLAYER1_MOVE_LEFT, playerSprites, 1, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(PLAYER1_MOVE_RIGHT, playerSprites, 1, 0, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);

        spriteHelper(PLAYER2_IDLE_LEFT, playerSprites, 0, 1, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(PLAYER2_IDLE_RIGHT, playerSprites, 0, 1, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
        spriteHelper(PLAYER2_MOVE_LEFT, playerSprites, 1, 1, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(PLAYER2_MOVE_RIGHT, playerSprites, 1, 1, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);

        spriteHelper(PLAYER3_IDLE_LEFT, playerSprites, 0, 2, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(PLAYER3_IDLE_RIGHT, playerSprites, 0, 2, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
        spriteHelper(PLAYER3_MOVE_LEFT, playerSprites, 1, 2, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(PLAYER3_MOVE_RIGHT, playerSprites, 1, 2, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);

        spriteHelper(PLAYER4_IDLE_LEFT, playerSprites, 0, 3, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(PLAYER4_IDLE_RIGHT, playerSprites, 0, 3, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
        spriteHelper(PLAYER4_MOVE_LEFT, playerSprites, 1, 3, Config.CharacterSpriteSize, Config.CharacterSpriteSize, true);
        spriteHelper(PLAYER4_MOVE_RIGHT, playerSprites, 1, 3, Config.CharacterSpriteSize, Config.CharacterSpriteSize, false);
    }

    private static void loadTileMap() {
        SpriteSheet tiles = new SpriteSheet(AssetLoader.loadImage(Config.TILEMAP));
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 5; x++) {
                TILEMAP[i++] = tiles.crop(x * Config.TileSpriteSize, y * Config.TileSpriteSize, Config.TileSpriteSize, Config.TileSpriteSize).build();
            }
        }

        SpriteSheet waitroom_tiles = new SpriteSheet(AssetLoader.loadImage(Config.WAITROOMTILEMAP));
        i = 0;
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 18; x++) {
                WAITROOM_TILEMAP[i++] = waitroom_tiles.crop(x * 16, y * 16, 16, 16).build();
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
