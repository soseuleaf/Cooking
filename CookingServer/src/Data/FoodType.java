package Data;

public enum FoodType {
    TEST(1, "테스트 데이터"),
    APPLE(2, "사과"),
    ;

    private final int index;
    private final String name;

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    FoodType(int index, String name) {
        this.index = index;
        this.name = name;
    }
}
