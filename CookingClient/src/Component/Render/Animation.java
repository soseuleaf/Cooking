package Component.Render;

import java.awt.image.BufferedImage;

public class Animation {
    private final int speed;
    private int index;
    private final BufferedImage[] frames;
    private long lastTime, timer;

    public Animation(int speed, BufferedImage[] frames) { // 여러장 애니메이션
        this.speed = speed;
        this.frames = frames;
        this.timer = 0;
        this.index = 0;
        this.lastTime = System.currentTimeMillis();
    }

    public Animation(int speed, BufferedImage sprite) { // 한장 애니메이션
        this.speed = speed;
        this.frames = new BufferedImage[1];
        this.frames[0] = sprite;
        this.timer = 0;
        this.index = 0;
        this.lastTime = System.currentTimeMillis();
    }

    private void update() {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if (timer > speed) {
            index = (index + 1) % frames.length;
            timer = 0;
        }
    }

    public BufferedImage getCurrentFrame() {
        update();
        return frames[index];
    }

    @Override
    public String toString() {
        return "Animation{" +
                "index=" + index +
                ", timer=" + timer +
                '}';
    }
}
