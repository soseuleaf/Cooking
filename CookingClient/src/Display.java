import Data.*;
import Data.KeyEventData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Collections;
import java.util.Vector;

public class Display {
    private final Vector<RenderData> renderDataVector = new Vector<>();
    private JFrame frame;
    private Canvas canvas;
    private KeyManager keyManager;
    private final CookTogether cookTogether;

    public Display(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
        createDisplay();
    }

    private void createDisplay() {
        frame = new JFrame(Config.Title);
        frame.setSize(Config.DisplayWidth, Config.DisplayHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);

        keyManager = new KeyManager();
        frame.addKeyListener(keyManager);

        canvas = new Canvas();
        canvas.setFocusable(false);
        canvas.setLocation(0, 0);
        canvas.setSize(Config.DisplayWidth, Config.DisplayHeight);
        canvas.setMaximumSize(new Dimension(Config.DisplayWidth, Config.DisplayHeight));
        canvas.setFocusable(false);

        frame.add(canvas);
        frame.pack();
    }

    private void updateDisplay(Graphics graphics) {
        Collections.sort(renderDataVector);
        for (RenderData renderDataItem : renderDataVector) {
            if (renderDataItem instanceof ImageRenderData data) {
                graphics.drawImage(
                        data.getSprite(),
                        data.getX(),
                        data.getY(),
                        Config.CharacterSize,
                        Config.CharacterSize,
                        null
                );
            } else if (renderDataItem instanceof StringRenderData data) {
                graphics.drawString(
                        data.getMessage(),
                        data.getX(),
                        data.getY()
                );
            } else {
                continue;
            }
        }
        renderDataVector.clear();
        keyManager.keyEventUpdate();
    }

    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();

        if (bufferStrategy == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.clearRect(0, 0, Config.DisplayWidth, Config.DisplayHeight);
        updateDisplay(graphics);

        bufferStrategy.show();
        graphics.dispose();
    }

    public void addRenderData(RenderData renderData) {
        renderDataVector.addElement(renderData);
    }

    public KeyEventData getKeyEventData() {
        return keyManager.getKetEventData();
    }
}
