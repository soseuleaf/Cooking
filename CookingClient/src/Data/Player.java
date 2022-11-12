package Data;

import java.awt.image.BufferedImage;

public class Player {
    private String name;
    private int x;
    private int y;
    private final int speed = 5;
    private final BufferedImage sprite;

    public Player() {
        this.name = "ME";
        this.x = 100;
        this.y = 200;
        this.sprite = new BufferedImage(16, 16, 1);
    }

    public Player(String name, int x, int y) {
        this.name = name;
        this.x = 100;
        this.y = 200;
        this.sprite = new BufferedImage(16, 16, 1);
    }

    public void moveX(int value) {
        x += (value * speed);
    }

    public void moveY(int value) {
        y += (value * speed);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getSprite(){
        return sprite;
    }

    public void setEvent(EventPacket eventPacket) {
        this.x = eventPacket.x;
        this.y = eventPacket.y;
    }
}
