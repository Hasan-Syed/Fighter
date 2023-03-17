package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.playerTypeEntities;

import java.awt.Point;
import org.json.JSONObject;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

public class remotePlayer extends superPlayer{

    public remotePlayer(gamePanel gp, boolean remoteControlled, JSONObject spawn) {
        super(gp, remoteControlled, spawn);
        init();
        //TODO Auto-generated constructor stub
    }
    void init(){
        worldPosition = new Point();
        this.hitbox = new Rectangle(0,0,45,45);
    }

    @Override
    public void update() {
        this.hitbox.setLocation(worldPosition);
        
    }

    @Override
    public void draw(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawString("client ID: " + ID, worldPosition.x, worldPosition.y- 10);
        g2d.fillRect(worldPosition.x, worldPosition.y, 45, 45);
    }

    @Override
    public void drawDebugMenu(Graphics2D g) {
        // TODO Auto-generated method stub
        
    }
    
}
