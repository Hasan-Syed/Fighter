package com.hasan.multiplayer.projects.flighter.game.HUD;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;

public class entityInfoDisplay {
    final gameObject entity;  // Tracked Entity
    final boolean spectator;  // Currently Spectated?
    boolean hudVisibility;
    // UI Settings
    Point positionMidPoint;   // Position
    Point position;
    Dimension size;

    public entityInfoDisplay (gameObject entity, boolean spectator, Point positionMidPoint){
        this.entity = entity;
        this.spectator = spectator;
        this.positionMidPoint = positionMidPoint;

        init();
    }

    void init (){
    }
    
    public void update (){
        hudVisibility = entity.hudVisible; // Update HUD Visibility
        size = new Dimension(100,20);
        position = new Point((positionMidPoint.x - size.width/2), (positionMidPoint.y - size.height));
    }

    public void draw (Graphics2D g2d){
        if (hudVisibility){
            Graphics2D infoDisplay = (Graphics2D) g2d.create(position.x, position.y, size.width, size.height);
            infoDisplay.setColor(Color.red);
            infoDisplay.fillRect(0, 0, size.width, size.height);
            infoDisplay.dispose();
        } else {

        }
    }
}
