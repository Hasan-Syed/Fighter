package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.interfaces.gunType;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

public class pistol extends superObject implements gunType, ActionListener {
    private status gunStatus;

    private boolean onCoolDown;
    private boolean onRecoilDown;

    private double cooldownAmnt;
    private double recoilDownAmnt;

    protected pistol(gamePanel gp, object whatsThis, boolean remoteControlled) {
        super(gp, whatsThis, remoteControlled);
        gunStatus = status.initializing;
        this.name = "Basic Piston";
        // TODO Auto-generated constructor stub
    }

    @Override
    public void loadAssets() {
        sprite = tools.loadScaledSprite(getClass(),
            "/com/hasan/multiplayer/projects/flighter/resources/gameResources/gun/gun.png", 32, 32);
        gunStatus = status.initialized;
    }

    @Override
    public void update() {
        /*
         * If the gun is not held by a entity, the gun cannot fire, so it returns early and breaks 
         */
        if (holder == null){
            gunStatus = status.notHeld;
        }
        /*
         * Check what Status the Weapon is on and continue with 
         */
        switch (gunStatus){
            case initialized -> {

            }
            case initializing -> {

            }
            case notHeld -> {
                
            }
            case onCooldown -> {

            }
            case onRecoil -> {

            }
            case ready -> {

            }
            case reloadRequired -> {

            }
            case reloading -> {

            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

    @Override
    public void drawDebugMenu(Graphics2D g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'drawDebugMenu'");
    }

    @Override
    public void fire() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fire'");
    }

    @Override
    public void reload() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reload'");
    }

}
