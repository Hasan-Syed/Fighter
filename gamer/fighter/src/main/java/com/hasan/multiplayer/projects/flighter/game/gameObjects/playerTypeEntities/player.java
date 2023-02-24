package com.hasan.multiplayer.projects.flighter.game.gameObjects.playerTypeEntities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;

import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.KeyHandler;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entityTypes.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.enums.playerEnums.playerDirection;
import com.hasan.multiplayer.projects.flighter.game.enums.playerEnums.playerFuelType;

public class player extends superPlayer {

    boolean collisionTop, collisionBottom, collisionLeft, collisionRight;
    KeyHandler keyH;

    public player(gamePanel gp, KeyHandler keyH, int ID) {
        super(gp);
        this.keyH = keyH;

        init();
    }

    void init() {
        this.name = "hasan";
        this.remoteControlled = false;
        this.visible = true;
        this.worldPosition = new Point(200, 0);
        this.playerType = com.hasan.multiplayer.projects.flighter.game.enums.playerEnums.playerType.player;
        this.fuelType = playerFuelType.health;
        this.maxHealth = this.cHealth = 100f;
        this.maxFuel = this.cFuel = 0f;
        this.maxSpeed = this.cSpeed = 5;
        this.direction = playerDirection.right;
        this.hitbox = new Rectangle(0, 0, 45, 45);
    }

    @Override
    public synchronized void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.gray);
        g2d.fillRect(worldPosition.x, worldPosition.y, 45, 45);
        if (overlap != null) {
            g2d.setColor(Color.RED);
            g2d.draw(overlap);

        }
        g2d.setColor(Color.CYAN);
        g2d.draw(hitbox);
    }

    @Override
    public synchronized void update() {

        if (keyH.upKey) {
            worldPosition.y -= (9.8 * gp.deltaTime) + 5;
        }
        if (worldPosition.y < gp.getHeight() && !keyH.upKey && !this.collisionBottom) {
            worldPosition.y += 9.8 * gp.deltaTime;
        }
        if (keyH.leftKey) {
            worldPosition.x -= cSpeed;
        }
        if (keyH.rightKey) {
            worldPosition.x += cSpeed;
        }
        // if (keyH.upKey) {
        // worldPosition.y -= cSpeed;
        // }
        // if (keyH.downKey) {
        // worldPosition.y += cSpeed;
        // }

        hitbox.setLocation(worldPosition);
        hitbox();
    }

    Rectangle overlap;

    synchronized void hitbox() {
        this.collisionBottom = false;
        this.collisionTop = false;
        this.collisionRight = false;
        this.collisionLeft = false;
        for (superPlayer entities : gp.players) {
            if (this.hitbox.intersects(entities.hitbox)) {
                // There is a collision

                // Get the overlapping rectangle
                overlap = this.hitbox.intersection(entities.hitbox);

                // Check which side the collision is happening
                if (overlap.width > overlap.height) {
                    // The collision is happening on the top or bottom
                    if (this.hitbox.y < entities.hitbox.y) {
                        this.collisionBottom = true;
                        // The collision is happening on the bottom
                        gp.player.worldPosition.y = this.hitbox.y = (int) entities.hitbox.y - entities.hitbox.height;
                    } else {
                        this.collisionTop = true;
                        // The collision is happening on the top
                        gp.player.worldPosition.y = this.hitbox.y = (int) entities.hitbox.y - entities.hitbox.height;
                    }
                } else {
                    // The collision is happening on the left or right
                    if (this.hitbox.x < entities.hitbox.x) {
                        // The collision is happening on the right
                        this.collisionRight = false;
                        gp.player.worldPosition.x = this.hitbox.x = (int) entities.hitbox.x - entities.hitbox.width;
                    } else {
                        // The collision is happening on the left
                        this.collisionLeft = false;
                        gp.player.worldPosition.x = this.hitbox.x = (int) entities.hitbox.x + entities.hitbox.width;
                    }
                }
            }
        }
    }

    @Override
    public void drawDebugMenu(Graphics g) {
        // TODO Auto-generated method stub
    }

}
