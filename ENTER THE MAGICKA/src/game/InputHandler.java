package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputListener;

//TODO: when player class is made, use mouseinput listener on it
//Also, NOTE to use isLeftMouseButton and isRightMouseButton(MouseEvent e) for player movement
public class InputHandler implements KeyListener, MouseInputListener {

    public InputHandler(Game game) {
        game.addKeyListener(this);
    }

    public class Key {
        private int numTimesPressed = 0;
        private boolean pressed = false;

        public void toggle(boolean isPressed) {
            pressed = isPressed;
            if (isPressed)
                numTimesPressed++;
        }

        public boolean isPressed() {
            return pressed;
        }

        public int getNumTimesPressed() {
            return numTimesPressed;
        }
    }

    public List<Key> keys = new ArrayList<Key>();

    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();

    @Override
    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void toggleKey(int keyCode, boolean isPressed) {
        if (keyCode == KeyEvent.VK_W) {
            up.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_S) {
            down.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_A) {
            left.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_D) {
            right.toggle(isPressed);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}