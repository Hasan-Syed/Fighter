package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.interfaces.weapon;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

public class dagger extends superObject implements weapon, ActionListener {
    Graphics2D g2d;
    private Timer animationTimer;
    private List<Image> sprites;
    public boolean basicAttack = true;
    public Line2D hitbox;

    public dagger(gamePanel gp) {
        super(gp, object.dagger);
        loadAssets();
        new tools();
        this.ID = tools.UUIDCreator();
        holder = (superPlayer) gp.player;
        this.name = "dagger";
        worldPosition = new Point(100, 100);
        // TODO Auto-generated constructor stub
    }

    public dagger(gamePanel gp, superPlayer owner) {
        super(gp, object.dagger);
        this.equippedBy = owner;
        loadAssets();
        new tools();
        this.ID = tools.UUIDCreator();
        holder = (superPlayer) gp.player;
        this.name = "dagger";
        worldPosition = new Point(100, 100);
        hitbox = new Line2D.Double(worldPosition.x,worldPosition.y,worldPosition.x + 32,worldPosition.y + 32);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void loadAssets() {
        sprite = tools.loadScaledSprite(getClass(),
                "/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger[0].png", 32, 32);
        List<String> spriteList = new ArrayList<>();
            spriteList.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger[0].png");
            spriteList.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger[45].png");
            spriteList.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger[90].png");
            spriteList.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger[180].png");
            spriteList.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger[225].png");
            spriteList.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger[270].png");
            spriteList.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger[315].png");
        sprites = tools.loadScaledSprite(getClass(), spriteList, 32,32);
    }
    boolean timeToSwitch = false;
    @Override
    public void update() {
        if (g2d != null)
            g2d.drawImage(sprite, 50, 50, Color.red, null);

        if (equippedBy != null) {
            this.worldPosition.setLocation(equippedBy.worldPosition);
        }
        if (basicAttack) {
            animationTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timeToSwitch = true;
                }
            });
            if (gp.keyH.shiftGearUp) {
                currentPhase = 0;
                animationTimer.start();
            }
            switch (holder.direction) {
                case left -> {

                }
                case right -> {

                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g2d = (Graphics2D) g;
        g2d.drawString("ID=" + ID, worldPosition.x, worldPosition.y - 10);
        if (basicAttack)
            g2d.drawImage(sprite, worldPosition.x, worldPosition.y, Color.BLUE, gp);
        if (basicAttack && timeToSwitch) {
            // TODO Auto-generated method stub
            switch (currentPhase) {
                case 0 -> {
                    worldPosition.x += 30;
                    worldPosition.y -= 5;
                    Point endPos = new Point(worldPosition.x + 32, worldPosition.y+ 32);
                    hitbox.setLine(worldPosition, endPos);
                    g2d.drawImage(sprites.get(4), worldPosition.x, worldPosition.y, null, null);
                    g2d.setColor(Color.CYAN);
                    g2d.fill(hitbox);
                    timeToSwitch = false;
                    currentPhase = 1;
                }
                case 1 -> {
                    worldPosition.x += 30;
                    worldPosition.y -= 5;
                    Point endPos = new Point(worldPosition.x + 32, worldPosition.y+ 32);
                    hitbox.setLine(worldPosition, endPos);
                    g2d.drawImage(sprites.get(5), worldPosition.x, worldPosition.y, null, null);
                    g2d.setColor(Color.CYAN);
                    g2d.fill(hitbox);
                    timeToSwitch = false;
                    currentPhase = 0;
                }
            }
        }
    }

    @Override
    public void drawDebugMenu(Graphics2D g) {
        // TODO Auto-generated method stub

    }

    @Override
    public void basicAttack() {

    }

    int currentPhase = 0;

    @Override
    public void impoweredAttack() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

}
