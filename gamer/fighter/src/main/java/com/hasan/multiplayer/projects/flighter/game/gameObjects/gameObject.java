package com.hasan.multiplayer.projects.flighter.game.gameObjects;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

/**
 * The gameObject is the superclass for all objects in the gam
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public abstract class gameObject {
    // Game Pamel
    public gamePanel gp;
    // Identity
    public int ID;
    public String name;
    public boolean remoteControlled;
    public Rectangle hitbox;
    // Visibility
    public boolean visible;
    // Location
    public Point worldPosition;
    protected gameObject (gamePanel gp) {
        this.gp = gp;
    }
    /**
     * The update() method is used to update the object
     */
    public abstract void update();
    /**
     * The draw(g) method is used to draw the object
     * @param g
     */
    public abstract void draw(Graphics g);
    /**
     * The drawDebugMenu(g) method is used to draw the object's debugMenu
     * @param g
     */
    public abstract void drawDebugMenu(Graphics g);
}
