package com.hasan.multiplayer.projects.flighter.game.gameObjects.objectTypes;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.json.JSONObject;
import java.awt.Point;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entityTypes.superPlayer;
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

    // Object Type Variable
    public final object whatsThis;
    // Health Related Variables
    public boolean destructible;
    public float maxHealth, cHealth;
    // Spawner and Holder's Information
    public int spawnedBy;
    public superPlayer holder;
    // Hitbox Settings
    public boolean ignorable;
    public Rectangle2D hitbox;
    // Objects Action Settings
    public JSONObject action;
    // Sprite of the Object
    public BufferedImage sprite;
    // Angle of the Object
    public int angle;

    protected superObject(gamePanel gp, object whatsThis) {
        super(gp);
        this.whatsThis = whatsThis;
        // TODO Auto-generated constructor stub
    }

    public abstract void loadAssets();

    public JSONObject getDelete() {
        JSONObject delete = new JSONObject()
                .put("object", whatsThis)
                .put("spawnedBy", spawnedBy)
                .put("ID", ID);
        return delete;
    }

    public void setSpawn(JSONObject spawn) {
        this.ID = spawn.getInt("ID");
        this.spawnedBy = spawn.getInt("spawnedBy");
        this.name = spawn.getString("name");
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
