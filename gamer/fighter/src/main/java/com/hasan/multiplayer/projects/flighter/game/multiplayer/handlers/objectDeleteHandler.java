package com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.enums.logger.objectDeleteHEnum;
import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;

public class objectDeleteHandler {
    final gamePanel gp;
    final multiplayer multiplayer;

    public objectDeleteHandler(gamePanel gp, multiplayer multiplayer) {
        this.gp = gp;
        this.multiplayer = multiplayer;
    }

    void logger(objectDeleteHEnum logType, Object message) {
        logType.println(message);
    }

    public void objectDelete(JSONArray objectDeletes) {
        JSONObject delete;
        if (objectDeletes.length() > 0) {
            objectDeletes = (JSONArray) objectDeletes.get(0);
            for (int index = 0; index != objectDeletes.length(); index++) {
                delete = objectDeletes.getJSONObject(index);
                // Get Object Details, to go Find the correct Item
                final object object = delete
                        .getEnum(com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object.class, "object");
                final int spawnerID = delete.getInt("spawnedBy");
                final int objectID = delete.getInt("ID");
                logger(objectDeleteHEnum.objectProcessing, "request to delete: objectType> " + object + ", spawnedBy> "
                        + spawnerID + ", objectID> " + objectID);
                logger(objectDeleteHEnum.objectProcessing, "Looking for: " + objectID);
                // Get ITEM, filter through first Object Type, spawned by, and then object ID
                List<superObject> objectForDelete = gp.gameObjects.stream()
                        .filter((deleteObject) -> deleteObject.whatsThis == object)
                        .filter((deleteObject) -> deleteObject.ID == objectID)
                        .filter((deleteObject) -> deleteObject.spawnedBy == spawnerID)
                        .collect(Collectors.toList());
                objectForDelete.forEach(obj -> System.out.println(obj.ID));
                // Select the top result and delete it
                if (objectForDelete.size() > 0) {
                    for (int indexIn = 0; indexIn <= gp.gameObjects.size()-1; indexIn++) {
                        if (gp.gameObjects.get(indexIn).ID == objectForDelete.get(0).ID) {
                            logger(objectDeleteHEnum.objectDeleted, "Deleted: " + objectID);
                            gp.gameObjects.remove(indexIn);
                        }
                    }
                } else {
                    logger(objectDeleteHEnum.objectDoesntExist, "object doesn't exist");
                }
            }
        }
    }
}
