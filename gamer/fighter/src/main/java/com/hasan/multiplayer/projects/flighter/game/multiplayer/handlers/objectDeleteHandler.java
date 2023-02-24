package com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.objectTypes.superObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;

public class objectDeleteHandler {
    final gamePanel gp;
    final multiplayer multiplayer;

    public objectDeleteHandler(gamePanel gp, multiplayer multiplayer) {
        this.gp = gp;
        this.multiplayer = multiplayer;
    }

    public void objectDelete(JSONArray objectDeletes) {
        JSONObject delete;
        if (objectDeletes.length() > 0){
            objectDeletes = (JSONArray) objectDeletes.get(0);
            for (int index = 0; index != objectDeletes.length(); index++) {
                delete = objectDeletes.getJSONObject(index);
                // Get Object Details, to go Find the correct Item
                object object = delete
                        .getEnum(com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object.class, "object");
                int spawnerID = delete.getInt("spawnerID");
                int objectID = delete.getInt("objectID");
                // Get ITEM
                superObject deleteItem = gp.gameObjects.stream().anyMatch(object -> {});
            }
        }
    }
}
