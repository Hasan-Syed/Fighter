package com.hasan.multiplayer.projects.flighter.game.gameObjects.entityTypes;

import java.util.List;

import org.json.JSONObject;

import java.awt.image.BufferedImage;

import com.hasan.multiplayer.projects.flighter.game.enums.playerEnums.playerDirection;
import com.hasan.multiplayer.projects.flighter.game.enums.playerEnums.playerFuelType;
import com.hasan.multiplayer.projects.flighter.game.enums.playerEnums.playerType;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

/**
 * superPlayer is used to Create Players and Characters.
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public abstract class superPlayer extends gameObject{
    // Player Settings
    public playerType playerType;
    public playerFuelType fuelType;
    // Player Values
    public float maxHealth, cHealth;
    public float maxFuel, cFuel;
    public int maxSpeed, cSpeed;
    public playerDirection direction;
    // Player Sprite(s)
    public BufferedImage activeSprite;
    public List<BufferedImage> sprites;
    
    /**
     * superPlayer(gp) is used to initialize the superPlayer class 
     * @param gp
     */
    protected superPlayer(gamePanel gp) {
        super(gp);
    }

    protected superPlayer (gamePanel gp, boolean remoteControlled, JSONObject spawn){
        super(gp);
        this.remoteControlled = remoteControlled;
        setPlayerSpawn(spawn);
    }

    public void setPlayerSpawn(JSONObject spawn){
        ID = spawn.getInt("ID");
        name = spawn.getString("name");
        playerType = spawn.getEnum(com.hasan.multiplayer.projects.flighter.game.enums.playerEnums.playerType.class, "playerType");
        fuelType = spawn.getEnum(com.hasan.multiplayer.projects.flighter.game.enums.playerEnums.playerFuelType.class, "fuelType");
        this.maxHealth = spawn.getJSONObject("health").getFloat("maxHealth");
        this.cHealth = spawn.getJSONObject("health").getFloat("cHealth");
        this.maxFuel = spawn.getJSONObject("fuel").getFloat("maxFuel");
        this.cFuel = spawn.getJSONObject("fuel").getFloat("cFuel");
        this.maxSpeed = spawn.getJSONObject("speed").getInt("maxSpeed");
        this.cSpeed = spawn.getJSONObject("speed").getInt("cSpeed");
    }
    public JSONObject getPlayerSpawn () {
        JSONObject spawn = new JSONObject()
            .put("ID", ID)
            .put("name", name)
            .put("playerType", playerType)
            .put("fuelType", fuelType)
            .put("health", new JSONObject()
                                    .put("maxHealth", maxHealth)
                                    .put("cHealth", cHealth))
            .put("fuel", new JSONObject()
                                    .put("maxFuel", maxFuel)
                                    .put("cFuel", cFuel))
            .put("speed", new JSONObject()
                                    .put("maxSpeed", maxSpeed)
                                    .put("cSpeed", cSpeed));
        return spawn;
    }
    public void setPlayerUpdate (JSONObject update){
        if (update.getInt("ID") == ID){
            this.maxHealth = update.getJSONObject("health").getFloat("maxHealth");
            this.cHealth = update.getJSONObject("health").getFloat("cHealth");
            this.maxFuel = update.getJSONObject("fuel").getFloat("maxFuel");
            this.cFuel = update.getJSONObject("fuel").getFloat("cFuel");
            this.maxSpeed = update.getJSONObject("speed").getInt("maxSpeed");
            this.cSpeed = update.getJSONObject("speed").getInt("cSpeed");
            this.worldPosition.x = update.getJSONObject("location").getInt("x");
            this.worldPosition.y = update.getJSONObject("location").getInt("y");
        }
    }
    public JSONObject getPlayerUpdate () {
        JSONObject udpate = new JSONObject()
            .put("ID", ID)
            .put("health", new JSONObject()
                                    .put("maxHealth", maxHealth)
                                    .put("cHealth", cHealth))
            .put("fuel", new JSONObject()
                                    .put("maxFuel", maxFuel)
                                    .put("cFuel", cFuel))
            .put("speed", new JSONObject()
                                    .put("maxSpeed", maxSpeed)
                                    .put("cSpeed", cSpeed))
            .put("location", new JSONObject()
                                    .put("x", worldPosition.getX())
                                    .put("y", worldPosition.getY()));
        return udpate;
    }
}
