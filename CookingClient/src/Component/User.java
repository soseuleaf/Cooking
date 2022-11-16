package Component;

import Component.Base.Character;
import Component.Packet.EventPacket;

public class User extends Character {
    public User(int x, int y, String name) {
        super(x, y, name);
    }

    public void bindEvent(EventPacket eventPacket) {
        if (eventPacket.x < x) { // If player is moving left
            isLookedRight = false;
            currentAnimation = animLeft;
        } else if (eventPacket.x > x) { //If player is moving right
            isLookedRight = true;
            currentAnimation = animRight;
        } else if (eventPacket.y != y) { //If player is moving up or down
            if (isLookedRight) currentAnimation = animRight;
            else currentAnimation = animLeft;
        } else { //If player is moving not moving
            if (isLookedRight) {
                currentAnimation = animIdleRight;
            } else {
                currentAnimation = animIdleLeft;
            }
        }
        this.x = eventPacket.x;
        this.y = eventPacket.y;
    }
}
