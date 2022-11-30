package Component.Type;

import lombok.Getter;

@Getter
public enum FoodType {
    SALMON("연어", 0),
    COOKED_SALMON("연어 스테이크", 1),
    BACON("베이컨", 2),
    COOKED_BACON("구운 베이컨", 3),
    BREAD("빵", 4),
    SLICED_BREAD("자른 빵", 5),
    POTATO("감자", 6),
    SLICED_POTATO("자른 감자", 7),
    FRENCH_FRIES("감자튀김", 8),
    MEAT("소고기", 9),
    COOKED_MEAT("조리된 소고기", 10),
    STEAK("스테이크", 11),
    MANDOO("만두", 12),
    FRIED_MANDOO("군만두", 13),
    EGG("계란", 14),
    FRIED_EGG("계란후라이", 15),
    CHICKEN("생닭", 16),
    FRIED_CHICKEN("치킨", 17),
    MUSHROOM("버섯", 18),
    MUSHROOM_SOUP("버섯 스프", 19),
    TOMATO("토마토", 20),
    TOMATO_SOUP("토마토 스프", 21),
    ONION("양파", 22),
    ONION_SOUP("양파 스프", 23),
    READY("준비", 0);

    private final String name;
    private final int spriteNum;

    FoodType(String name, int spriteNum) {
        this.name = name;
        this.spriteNum = spriteNum;
    }
}
