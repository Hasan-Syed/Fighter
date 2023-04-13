package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType;

import org.json.JSONObject;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

/**
 * superObject class is used to create in-game objects, those objects can
 * implement other classes to transform the objects into action objects.
 * 
 * @author Hasan Syed
 * @since 1.0
 * @version 1.0
 */
public abstract class superObject extends gameObject {
    // Equipped by
    public superPlayer equippedBy;
    // Object Type Variable
    public final object whatsThis;
    // Health Related Variables
    public boolean destructible;
    public float maxHealth, cHealth;
    // Spawner and Holder's Information
    public int spawnedBy;
    public superPlayer spawnedByE;
    public superPlayer holder;
    // Hitbox Settings
    public boolean ignorable;
    public Rectangle hitbox;
    // Objects Action Settings
    public JSONObject action;
    // Sprite of the Object
    public Image sprite;
    public int currentSprite = 0;
    // Angle of the Object
    public int angle;
    public boolean interactable;

    protected superObject(gamePanel gp, object whatsThis, boolean remoteControlled) {
        super(gp, com.hasan.multiplayer.projects.flighter.game.gameObjects.enums.gameObjectType.object);
        this.whatsThis = whatsThis;
        this.remoteControlled = remoteControlled;
    }

    protected abstract void loadAssets();

    public JSONObject getDelete() {
        JSONObject delete = new JSONObject()
                .put("object", whatsThis)
                .put("spawnedBy", gp.multiplayer.clientID)
                .put("ID", ID);
        return delete;
    }

    @Override
    public void draw(Graphics2D g2d){
        g2d.drawImage(sprite, worldPosition.x, worldPosition.y, gp);
    }

    public void setUpdate(JSONObject update) {
        this.ID = update.getInt("ID");
        this.spawnedBy = update.getInt("spawnedBy");
        this.name = update.getString("name");
        this.currentSprite = update.getInt("currentSprite");
        this.interactable = update.getBoolean("interactable");
        this.remoteControlled = update.getBoolean("remote-Controlled");
        // Health
        {
            JSONObject health = update.getJSONObject("health");
            this.destructible = health.getBoolean("destructible");
            this.maxHealth = health.getFloat("maxHealth");
            this.cHealth = health.getFloat("cHealth");
        }
        // Location
        {
            JSONObject location = update.getJSONObject("location");
            this.worldPosition = new Point();
            this.worldPosition.x = location.getInt("x");
            this.worldPosition.y = location.getInt("y");
            this.angle = location.getInt("angle");
        }
    }
    public JSONObject getUpdate() {
        JSONObject update = new JSONObject()
                .put("ID", ID)
                .put("spawnedBy", spawnedBy)
                .put("name", name)
                .put("currentSprite", currentSprite)
                .put("interactable", interactable)
                .put("health", new JSONObject()
                        .put("destructible", destructible)
                        .put("maxHealth", maxHealth)
                        .put("cHealth", cHealth))
                .put("location", new JSONObject()
                        .put("x", worldPosition.getX())
                        .put("y", worldPosition.getY())
                        .put("angle", angle));
        return update;
    }
    public void setSpawn(JSONObject spawn) {
        this.ID = spawn.getInt("ID");
        this.spawnedBy = spawn.getInt("spawnedBy");
        this.name = spawn.getString("name");
        this.interactable = spawn.getBoolean("interactable");
        this.remoteControlled = spawn.getBoolean("remote-Controlled");
        // Health
        {
            JSONObject health = spawn.getJSONObject("health");
            this.destructible = health.getBoolean("destructible");
            this.maxHealth = health.getFloat("maxHealth");
            this.cHealth = health.getFloat("cHealth");
        }
        // Location
        {
            JSONObject location = spawn.getJSONObject("location");
            this.worldPosition = new Point();
            this.worldPosition.x = location.getInt("x");
            this.worldPosition.y = location.getInt("y");
            this.angle = location.getInt("angle");
        }
    }
    public JSONObject getSpawn() {
        JSONObject spawn = new JSONObject()
                .put("ID", ID)
                .put("spawnedBy", gp.multiplayer.clientID)
                .put("object", whatsThis)
                .put("name", name)
                .put("interactable", interactable)
                .put("health", new JSONObject()
                        .put("destructible", destructible)
                        .put("maxHealth", maxHealth)
                        .put("cHealth", cHealth))
                .put("location", new JSONObject()
                        .put("x", worldPosition.getX())
                        .put("y", worldPosition.getY())
                        .put("angle", angle));
        return spawn;
    }
}
