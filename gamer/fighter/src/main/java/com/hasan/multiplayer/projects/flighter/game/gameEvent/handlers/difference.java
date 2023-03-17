package com.hasan.multiplayer.projects.flighter.game.gameEvent.handlers;

import java.util.TimerTask;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;

public class difference extends TimerTask{
    
    private final float effectAmount;
    private final int duration;
    private final double incrementRate;
    private final gameObject effectedEntity;
    private int upTime;

    public difference (float effectAmount, int duration, gameObject effectedEntity){
        this.effectAmount = effectAmount;
        this.duration = duration;
        this.upTime = this.duration;
        this.effectedEntity = effectedEntity;
        this.incrementRate = this.effectAmount/this.duration;
    }

    @Override
    public void run() {
        if (upTime == 0) {
            this.cancel();
            return;
        }
        if (upTime >= 1){
            upTime -= 1;
            if (effectedEntity.maxHealth >= (effectedEntity.cHealth + incrementRate)){
                effectedEntity.cHealth += incrementRate;
            }
        }
    }
    
}
