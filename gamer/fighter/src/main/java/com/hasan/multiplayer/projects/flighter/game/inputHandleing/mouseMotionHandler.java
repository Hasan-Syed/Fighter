package com.hasan.multiplayer.projects.flighter.game.inputHandleing;

import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * Handles Mouse Motion Inputs
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class mouseMotionHandler implements MouseMotionListener {
    public Point mouseLoc = new Point();

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        mouseLoc = new Point(e.getX(), e.getY());
    }
}
