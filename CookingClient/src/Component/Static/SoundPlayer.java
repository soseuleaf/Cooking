package Component.Static;

import Component.Render.AssetLoader;
import Component.Type.SoundType;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class SoundPlayer {
    private static Clip background;

    public static Clip getClip(String path, boolean loop) {
        try {
            AudioInputStream audioStream = AssetLoader.loadAudio(path);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(loop ? Clip.LOOP_CONTINUOUSLY : 0);
            return clip;
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void play(SoundType soundType) {
        getClip(soundType.getPath(), false).start();
    }

    public static void stopBackground() {
        if (background != null) {
            background.stop();
            background.close();
        }
    }

    public static void playLoop(SoundType soundType) {
        stopBackground();
        background = getClip(soundType.getPath(), true);
        background.start();
    }
}

