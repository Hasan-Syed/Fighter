package com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.enums.logger.objectSpawnHEnum;
import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.objectTypes.superObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;;

public class objectSpawnHandler {
    final gamePanel gp;
    final multiplayer multiplayer;

    public objectSpawnHandler(gamePanel gp, multiplayer multiplayer) {
        this.gp = gp;
        this.multiplayer = multiplayer;
    }

    void logger(objectSpawnHEnum logType, object object, Object message) {
        logType.println(object,message);
    }


    public void objectSpawn(JSONArray objectSpawns) {
        JSONObject spawn;
        if (objectSpawns.length() > 0){
            objectSpawns = (JSONArray) objectSpawns.get(0);
            for (int index = 0; index != objectSpawns.length(); index++) {
                spawn = objectSpawns.getJSONObject(index);
                // Get Object Type
                object object = spawn
                        .getEnum(com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object.class, "object");
                logger(objectSpawnHEnum.objectProcessing, object, "");
                superObject objectToBeSpawned;
                switch (object) {
                    case block -> {

                    }
                    case dagger -> {
                        objectToBeSpawned = object.getObject(gp);
                        objectToBeSpawned.setSpawn(spawn);
                        gp.gameObjects.add(objectToBeSpawned);
                        logger(objectSpawnHEnum.objectSpawned, object, "Object Spawned");
                    }
                }
            }
        }
    }
}
