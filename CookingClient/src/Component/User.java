package Component;

import Component.Base.Character;
import Component.Packet.UserPacket;
import Component.Static.Assets;

public class User extends Character {
    public User(int x, int y, String name) {
        super(x, y, name);
    }

    public void bindEvent(UserPacket userPacket) {
        if (userPacket.getX() < x) { // If player is moving left
            isLookedRight = false;
            currentAnimation = animLeft;
        } else if (userPacket.getX() > x) { //If player is moving right
            isLookedRight = true;
            currentAnimation = animRight;
        } else if (userPacket.getY() != y) { //If player is moving up or down
            if (isLookedRight) currentAnimation = animRight;
            else currentAnimation = animLeft;
        } else { //If player is moving not moving
            if (isLookedRight) {
                currentAnimation = animIdleRight;
            } else {
                currentAnimation = animIdleLeft;
            }
        }
        this.x = userPacket.getX();
        this.y = userPacket.getY();

        if(userPacket.getFoodType() != null){
            holdFood = Assets.FOODLIST.get(userPacket.getFoodType().ordinal()).clone();
        } else{
            holdFood = null;
        }
    }
}
