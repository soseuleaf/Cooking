package Main;

import Component.DTO.KeyEventData;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private final boolean[] keys;
    private KeyEventData keyEventData;
    private boolean onSpace;
    private boolean onQ;
    private boolean onE;

    public KeyManager() {
        keys = new boolean[256];
        keyEventData = new KeyEventData(false, false, false, false, false, false, false);
    }

    public void keyEventUpdate() {
        keyEventData = new KeyEventData(
                keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP],
                keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT],
                keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN],
                keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT],
                onSpace,
                onQ,
                onE);
        onSpace = false;
        onQ = false;
        onE = false;
    }

    public KeyEventData getKetEventData() {
        return keyEventData;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) onSpace = true;
        if (e.getKeyCode() == KeyEvent.VK_Q) onQ = true;
        if (e.getKeyCode() == KeyEvent.VK_E) onE = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}