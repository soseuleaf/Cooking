import Data.Config;
import Data.KeyEventData;
import Data.RenderData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Vector;

public class Display {
    private JFrame frame;
    private Canvas canvas;
    private KeyManager keyManager;
    private CookTogether cookTogether;

    private final Vector<RenderData> renderDataVector = new Vector<>();

    public Display(CookTogether cookTogether){
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

    private void updateDisplay(Graphics graphics){
        for(RenderData renderDataItem : renderDataVector) {
            graphics.drawImage(
                    renderDataItem.sprite(),
                    renderDataItem.x(),
                    renderDataItem.y(),
                    null
            );
        }
        renderDataVector.clear();
        keyManager.keyEventUpdate();
    }

    public void render(){
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();

        if(bufferStrategy == null){
            canvas.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.clearRect(0, 0, Config.DisplayWidth, Config.DisplayHeight);

        updateDisplay(graphics);
        bufferStrategy.show();
        graphics.dispose();
    }

    public void addRenderData(RenderData renderData){
        renderDataVector.add(renderData);
    }

    public KeyEventData getKeyEventData(){
        return keyManager.getKetEventData();
    }
}
