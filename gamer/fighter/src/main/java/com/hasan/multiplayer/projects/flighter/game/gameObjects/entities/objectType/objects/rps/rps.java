package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.json.JSONObject;

import java.awt.Image;

import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;
import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.KeyHandler;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.mouseMotionHandler;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

public class rps extends superObject {
    // Input Handle
    private final KeyHandler keyH;
    // Display
    Image displayIconClr, displayIconGry;   // Image Sprites
    private Dimension dISize;   // Main Sprite Size
    private boolean alerted;    // Player Interact alerted?
    public rpsBackWorks backWorks;
    public rpsGameLobby rpsGameLobby;
    private int miniConnectionPort = 5621;
    private boolean attemptingConnection;
    protected mouseMotionHandler mMotionH;

    public rps(gamePanel gp, superPlayer spawnedBy, boolean remoteControlled) {
        super(gp, object.Rock_Paper_Scissors, remoteControlled);
        this.keyH = gp.keyH;
        this.backWorks = new rpsBackWorks(gp, this);
        this.spawnedByE = spawnedBy;
        init();
        // TODO Auto-generated constructor stub
    }

    private void init() {
        // Setup Object
        this.name = "RPS";
        this.ID = tools.UUIDCreator();
        this.worldPosition = new Point(10, 10);
        this.dISize = new Dimension(150, 100);
        this.hitbox = new Rectangle(worldPosition, dISize);
        this.interactable = true;
        this.mMotionH = gp.mMotionH;
        // Load Assets
        loadAssets();
    }

    protected void loadAssets() {
        displayIconClr = tools.loadScaledSprite(getClass(),
                "/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/challenger.gif", dISize.width,
                dISize.height);
        displayIconGry = tools.loadScaledSprite(getClass(),
                "/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/challenger(grayscale).gif",
                dISize.width, dISize.height);
    }

    @Override
    public void update() {
        backWorks.update();
        // Escape screen
        if (keyH.escape){
            keyH.escape = false;
            gp.player.engaged = false;
            backWorks.queueDisconnect = true;
        }
        // Update hitbox
        {
            this.hitbox.setLocation(worldPosition);
        }
        // Check for player Collision
        {
            if (this.hitbox.intersects(gp.player.hitbox) && !alerted) {
                gp.hud.addNewTopNotification(ID, "press \"E\" to interact", 4, "[superObject][rps]",
                notifyType.info);
                alerted = true;
            } else if (!this.hitbox.intersects(gp.player.hitbox) && alerted) {
                alerted = false;
            }
            
            if (this.hitbox.intersects(gp.player.hitbox) && keyH.interactKey) {
                keyH.interactKey = false;
                attemptingConnection = true;
                if (attemptingConnection){
                    try {
                        // Add player to the backWorks connected player List
                        tools.addPlayerMini(backWorks.connectedPlayers, gp.player);
                        //gp.player.engaged = true;
                        rpsGameLobby = new rpsGameLobby(gp, this, miniConnectionPort);
                        gp.hud.addNewTopNotification(ID, "connected to " + spawnedByE.name + "'s lobby", 3, name, notifyType.success);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @Override
    public void draw(Graphics2D g) {
        if (gp.player.engaged){
            backWorks.draw(g);
        }
        Graphics2D rps = (Graphics2D) g.create(worldPosition.x, worldPosition.y, dISize.width, dISize.height);
        if (interactable) {
            // If the game is interactable, show ICON in full colors
            rps.drawImage(displayIconClr, 0, 0, gp);
        } else {
            // IF the game is not interactable, show ICON is black&white
            rps.drawImage(displayIconGry, 0, 0, gp);
        }
    }
    @Override
    public void drawDebugMenu(Graphics2D g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'drawDebugMenu'");
    }
    public void setUpdate(JSONObject update) {
        this.ID = update.getInt("ID");
        this.spawnedBy = update.getInt("spawnedBy");
        this.name = update.getString("name");
        this.currentSprite = update.getInt("currentSprite");
        this.interactable = update.getBoolean("interactable");
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
        // Miscellaneous
        {
            JSONObject miscellaneous = spawn.getJSONObject("miscellaneous");
            this.miniConnectionPort = miscellaneous.getInt("miniConnectionPort");
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
                        .put("angle", angle))
                .put("miscellaneous", new JSONObject()
                        .put("miniConnectionPort", miniConnectionPort));
        return spawn;
    }

}
