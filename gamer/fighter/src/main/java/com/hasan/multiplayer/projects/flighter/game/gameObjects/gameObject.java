package com.hasan.multiplayer.projects.flighter.game.gameObjects;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.enums.gameObjectType;

/**
 * The gameObject is the superclass for all objects in the gam
 * 
 * @author Hasan Syed
 * @version 1.2
 * @since 1.0
 */
public abstract class gameObject {
    // Game Pamel
    public final gamePanel gp;
    public final gameObjectType gameObjectType;
    // Identity
    public int ID;
    public String name;
    public boolean remoteControlled;
    public boolean alive;
    public Rectangle hitbox;
    // Visibility
    public boolean visible;
    public boolean hudVisible;
    // Location
    public Point worldPosition;
    protected gameObject (gamePanel gp, gameObjectType gameObjectType) {
        this.gp = gp;
        this.gameObjectType = gameObjectType;
    }
    // Health Settings
    public float maxHealth, cHealth;
    public boolean healthManipulatable;
    // Movement Settings
    public boolean movable;
    public boolean slowed;
    /**
     * The update() method is used to update the object
     */
    public abstract void update();
    /**
     * The draw(g) method is used to draw the object
     * @param g
     */
    public abstract void draw(Graphics2D g);
    /**
     * The drawDebugMenu(g) method is used to draw the object's debugMenu
     * @param g
     */
    public abstract void drawDebugMenu(Graphics2D g);

    public abstract JSONObject getSpawn();
    public abstract void setSpawn (JSONObject spawn);
    public abstract JSONObject getUpdate();
    public abstract void setUpdate (JSONObject Update);
}
