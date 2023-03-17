package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Image;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;
import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.KeyHandler;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

public class rps extends superObject {

    // Input Handle
    private final KeyHandler keyH;
    // Display
    Image displayIconClr, displayIconGry;   // Image Sprites
    private Dimension dISize;   // Main Sprite Size
    private boolean alerted;    // Player Interact alerted?

    public rps(gamePanel gp) {
        super(gp, object.Rock_Paper_Scissors);
        this.keyH = gp.keyH;
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
        // Update hitbox
        {
            this.hitbox.setLocation(worldPosition);
        }
        // Check for player Collision
        {
            for (superPlayer entity : gp.players) {
                if (this.hitbox.intersects(entity.hitbox)) {

                }
            }
            if (this.hitbox.intersects(gp.player.hitbox) && !alerted) {
                gp.hud.addNewTopNotification(ID, "press \"E\" to interact", 4, "[superObject][rps]",
                        notifyType.info);
                alerted = true;
            } else if (!this.hitbox.intersects(gp.player.hitbox) && alerted) {
                alerted = false;
            }

            if (this.hitbox.intersects(gp.player.hitbox) && keyH.interactKey) {
                System.out.println("Hello");
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
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

    @Override
    public JSONObject getSpawn() {
        JSONObject spawn = new JSONObject()
                .put("ID", ID)
                .put("name", name)
                .put("interactable", interactable)
                .put("location", new JSONObject()
                        .put("x", worldPosition.getX())
                        .put("y", worldPosition.getY()));
        return spawn;
    }
    @Override
    public void setSpawn(JSONObject spawn) {
        this.ID = spawn.getInt("ID");
        this.name = spawn.getString("name");
        this.interactable = spawn.getBoolean("interactable");
        this.worldPosition.x = spawn.getJSONObject("location").getInt("x");
        this.worldPosition.y = spawn.getJSONObject("location").getInt("y");
    }
    @Override
    public JSONObject getUpdate() {
        JSONObject update = new JSONObject()
                .put("ID", ID)
                .put("name", name)
                .put("interactable", interactable)
                .put("location", new JSONObject()
                        .put("x", worldPosition.getX())
                        .put("y", worldPosition.getY()));
        return update;
    }
    @Override
    public void setUpdate(JSONObject update) {
        if (this.ID == update.getInt("ID")) {
            this.name = update.getString("name");
            this.interactable = update.getBoolean("interactable");
            this.worldPosition.x = update.getJSONObject("location").getInt("x");
            this.worldPosition.y = update.getJSONObject("location").getInt("y");
        }
    }
}
