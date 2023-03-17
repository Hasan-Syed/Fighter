package com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;

public class objectUpdateHandler {
    final gamePanel gp;
    final multiplayer multiplayer;

    public objectUpdateHandler(gamePanel gp, multiplayer multiplayer) {
        this.gp = gp;
        this.multiplayer = multiplayer;
    }

    public void updateObject(JSONArray objectUpdates) {
        JSONObject update;
        for (int index = 0; index != objectUpdates.length(); index++) {
            update = objectUpdates.getJSONObject(index);
            // get Sorting Detes
            final object object = update
                    .getEnum(com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object.class, "object");
            final int spawnerID = update.getInt("spawnedBy");
            final int objectID = update.getInt("ID");
            // Get ITEM, filter through first Object Type, spawned by, and then object ID
            List<superObject> objectForUpdate = gp.gameObjects.stream()
                    .filter((updateObject) -> updateObject.whatsThis == object)
                    .filter((updateObject) -> updateObject.ID == objectID)
                    .filter((updateObject) -> updateObject.spawnedBy == spawnerID)
                    .collect(Collectors.toList());
            objectForUpdate.get(0).setUpdate(update);
        }
    }
}
