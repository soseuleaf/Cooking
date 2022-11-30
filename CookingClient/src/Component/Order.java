package Component;

import Component.Type.FoodType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Order {
    private final UUID uuid;
    private final FoodType foodType;
    private final double maxTime;
    private double nowTime;

    private Order(UUID uuid, FoodType foodType, double maxTime, double nowTime) {
        this.uuid = uuid;
        this.foodType = foodType;
        this.maxTime = maxTime;
        this.nowTime = nowTime;
    }

    public void updateTime() {
        nowTime--;
    }

    public Order updateTime(double time) {
        nowTime -= time;
        return this;
    }

    public boolean isExpirationOrder() {
        return nowTime < 0;
    }

    public static Order NewOrder(UUID uuid, FoodType foodType) {
        return new Order(uuid, foodType, 40, 40);
    }

    @Override
    public String toString() {
        return foodType.getName() + " time: " + getNowTime();
    }

}
