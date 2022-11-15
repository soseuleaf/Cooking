package Data;

import lombok.Getter;

@Getter
public enum FoodType {
    TEST("테스트 데이터", 9),
    APPLE("사과", 5),
    MEAT("고기", 12),
    ;

    private final String name;
    private final int spriteNum;

    FoodType(String name, int spriteNum) {
        this.name = name;
        this.spriteNum = spriteNum;
    }
}
