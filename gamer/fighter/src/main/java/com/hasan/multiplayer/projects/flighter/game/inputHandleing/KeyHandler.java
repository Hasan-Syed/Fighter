package com.hasan.multiplayer.projects.flighter.game.inputHandleing;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyEvent;

/**
 * Handles Keyboards Inputs
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class KeyHandler implements KeyListener {

    public boolean escape;
    public boolean upKey, downKey, leftKey, rightKey; // Character movement Keys
    public boolean interactKey;
    public boolean multipleyerThrow;
    public boolean shiftGearUp, shiftGearDown;
    public List<Boolean> ability = new ArrayList<>();

    public KeyHandler() {
        init();
    }

    void init() {
        ability.add(false);
        ability.add(false);
        ability.add(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_SHIFT -> {
                shiftGearUp = true;
            }
            case KeyEvent.VK_CONTROL -> {
                shiftGearDown = true;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upKey = true;
        } else if (code == KeyEvent.VK_S) {
            downKey = true;
        } else if (code == KeyEvent.VK_A) {
            leftKey = true;
        } else if (code == KeyEvent.VK_D) {
            rightKey = true;
        }
        switch (code) {
            case KeyEvent.VK_1 -> {
                ability.set(0, true);
            }
            case KeyEvent.VK_2 -> {
                ability.set(1, true);
            }
            case KeyEvent.VK_3 -> {
                ability.set(2, true);
            }
            case KeyEvent.VK_SHIFT -> {
                shiftGearUp = true;
            }
            case KeyEvent.VK_CONTROL -> {
                shiftGearDown = true;
            }
            case KeyEvent.VK_E -> {
                interactKey = true;
            }
            case KeyEvent.VK_Q -> {
                multipleyerThrow = true;
            }
            case KeyEvent.VK_ESCAPE -> {
                escape = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upKey = false;
        } else if (code == KeyEvent.VK_S) {
            downKey = false;
        } else if (code == KeyEvent.VK_A) {
            leftKey = false;
        } else if (code == KeyEvent.VK_D) {
            rightKey = false;
        }
        switch (code) {
            case KeyEvent.VK_1 -> {
                ability.set(0, false);
            }
            case KeyEvent.VK_2 -> {
                ability.set(1, false);
            }
            case KeyEvent.VK_3 -> {
                ability.set(2, false);
            }
            case KeyEvent.VK_SHIFT -> {
                shiftGearUp = false;
            }
            case KeyEvent.VK_CONTROL -> {
                shiftGearDown = false;
            }
            case KeyEvent.VK_E -> {
                interactKey = false;
            }
            case KeyEvent.VK_Q -> {
                multipleyerThrow = false;
            }
            case KeyEvent.VK_ESCAPE -> {
                escape = false;
            }
        }
    }

}
