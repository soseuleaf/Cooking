package Component.Render;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class AssetLoader {
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(getResources(path)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static InputStream loadText(String path) {
        try {
            return Objects.requireNonNull(getResources(path)).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static AudioInputStream loadAudio(String path) {
        try {
            return AudioSystem.getAudioInputStream(getResources(path));
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static URL getResources(String path) {
        return AssetLoader.class.getResource(path);
    }
}
