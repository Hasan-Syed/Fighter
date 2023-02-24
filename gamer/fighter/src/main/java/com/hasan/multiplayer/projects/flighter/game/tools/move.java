package com.hasan.multiplayer.projects.flighter.game.tools;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;

import java.awt.Point;
import java.util.TimerTask;

public class move extends TimerTask{
    final gameObject object;
    final Point targetPosition;
    final int angle;
    final int speed;

    /**
     * Initializes the Move Class
     * 
     * @param object The Object that needs to be moved
     * @param targetPosition The end Position
     * @param angle 
     * @param speed
     */
    public move(gameObject object, Point targetPosition, int angle, int speed) {
        this.object = object;
        this.targetPosition = targetPosition;
        this.angle = angle;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (object.worldPosition.equals(targetPosition)) {
            object.worldPosition.x += (int) (speed * Math.cos(angle));
            object.worldPosition.y += (int) (speed * Math.sin(angle));
        }
    }
}