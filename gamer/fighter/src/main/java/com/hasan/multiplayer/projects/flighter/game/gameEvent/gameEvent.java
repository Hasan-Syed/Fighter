package com.hasan.multiplayer.projects.flighter.game.gameEvent;

import java.util.List;

import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.gameEvent.enums.gameEventType;
import com.hasan.multiplayer.projects.flighter.game.gameEvent.handlers.difference;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

/**
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public abstract class gameEvent {
    public final gamePanel gp;
    public final int ID;
    // Game Event Settings
    public difference difference;
    public final gameEventType eventType;
    public final gameObject sourceEntity;
    public final gameObject targetEntity;
    public final boolean dualCompatable;
    // Event
    final boolean timed;
    final double duration;
    // Effect Settings
    final double effectAmount;

    /**
     * 
     * @param gp 
     * @param ID ID of Event
     * @param eventType event Type
     * @param sourceEntity The source entity
     * @param targetEntity The targeted entity 
     * @param effect effect package
     */
    public gameEvent(gamePanel gp, 
                     int ID,
                     gameEventType eventType,
                     gameObject sourceEntity,
                     gameObject targetEntity,
                     JSONObject effect) {
        this.gp = gp;
        this.ID = ID;
        this.eventType = eventType;
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.dualCompatable = effect.getBoolean("dualCompatable");
        this.timed = effect.getJSONObject("timing").getBoolean("timed");
        this.duration = effect.getJSONObject("timing").getDouble("duration");
        this.effectAmount = effect.getJSONObject("effects").getDouble("effectAmount");
    }

    public void remove (List<gameEvent> mainList){
        difference.cancel();
        var toBeRemoved = mainList.stream()
                            .filter((removal) -> removal.ID == ID)
                            .filter((removal) -> removal.eventType == eventType)
                            .findFirst().get();
        mainList.remove(toBeRemoved);
    }

    public String toString(){
        return "[gameEvent] -> ID: " + ID + "\n" + 
               "[gameEvent] -> gameEventType: " + gameEventType.values() + "\n" +
               "[gameEvent] -> timed, Duration: " + timed + ", " + duration + "\n" +
               "[gameEvent] -> sourceEntity: [gameObject] -> ID, name" + sourceEntity.ID + ", " + sourceEntity.name + "\n" +
               "[gameEvent] -> targetEntity: [gameObject] -> ID, name" + targetEntity.ID + ", " + targetEntity.name;
    }
}