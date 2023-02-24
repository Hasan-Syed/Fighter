package com.hasan.multiplayer.projects.flighter.game.gameObjects.object;

import java.awt.Graphics;
import java.awt.Point;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entityTypes.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.objectTypes.superObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.interfaces.weapon;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

public class dagger extends superObject implements weapon{
    public boolean basicAttack= true;

    public dagger(gamePanel gp) {
        super(gp, object.dagger);
        loadAssets();
        new tools();
        this.ID = tools.UUIDCreator();
        holder = (superPlayer) gp.player;
        this.name = "dagger";
        worldPosition = new Point(100, 100);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void loadAssets() {
        sprite = tools.loadScaledSprite(getClass(), "/com/hasan/multiplayer/projects/flighter/resources/gameResources/dagger/dagger.png", 32, 32);
    }

    @Override
    public void update() {
        
        if (basicAttack) {
            switch (holder.direction){
                case left -> {

                }
                case right -> {

                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sprite, worldPosition.x, worldPosition.y, null, gp);
    }

    @Override
    public void drawDebugMenu(Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void basicAttack() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void impoweredAttack() {
        // TODO Auto-generated method stub
        
    }
    
}
