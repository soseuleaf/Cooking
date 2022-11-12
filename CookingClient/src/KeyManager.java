import Data.KeyEventData;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private final boolean[] keys;
    private KeyEventData keyEventData;

    public KeyManager() {
        keys = new boolean[256];
    }

    public void keyEventUpdate() {
        keyEventData = new KeyEventData(
                keys[KeyEvent.VK_W],
                keys[KeyEvent.VK_A],
                keys[KeyEvent.VK_S],
                keys[KeyEvent.VK_D],
                keys[KeyEvent.VK_SPACE]);
    }

    public KeyEventData getKetEventData(){
        return keyEventData;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}