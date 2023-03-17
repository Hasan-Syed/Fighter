package com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.enums.logger.objectSpawnHEnum;
import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
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
        logType.println(object, message);
    }

    public void objectSpawn(JSONArray objectSpawns) {
        JSONObject spawn;
        if (objectSpawns.length() > 0) {
            objectSpawns = (JSONArray) objectSpawns.get(0);
            System.out.println(objectSpawns.length());
            for (int index = 0; index != objectSpawns.length(); index++) {
                spawn = objectSpawns.getJSONObject(index);
                // Get Object Type
                final object object = spawn.getEnum(com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object.class, "object");
                final int spawnerID = spawn.getInt("spawnedBy");
                final int objectID = spawn.getInt("ID");
                logger(objectSpawnHEnum.objectProcessing, object, "Checking if the Object already exists");
                List<superObject> alreadySpawnCheck = gp.gameObjects.stream()
                            .filter((spawnCheck) -> spawnCheck.whatsThis == object)
                            .filter((spawnCheck) -> spawnCheck.spawnedBy == spawnerID)
                            .filter((spawnCheck) -> spawnCheck.ID == objectID)
                            .collect(Collectors.toList());
                if (alreadySpawnCheck.size() > 0){
                    logger(objectSpawnHEnum.objectAlreadyExists, object, "The Object already exists");
                } else {
                    logger(objectSpawnHEnum.objectProcessing, object, "The Object was not found, processing spawn request");
                    logger(objectSpawnHEnum.objectProcessing, object, "Looking of the object owner/spawnedBy value");
                    superPlayer spawnedBy = null;
                    List<superPlayer> spawnedByML = gp.players.stream()
                                .filter((spawner) -> spawner.ID == spawnerID)
                                .collect(Collectors.toList());
                    if (spawnedByML.size() == 0){
                        spawnedBy = gp.player;
                        logger(objectSpawnHEnum.objectProcessing, object, "The Object is spawned by my client; UN=" + spawnedBy.name);
                    } else {
                        spawnedBy = spawnedByML.get(0);
                        logger(objectSpawnHEnum.objectProcessing, object, "The Object is spawned by an external client; UN=" + spawnedBy.name + ", ID=" + spawnedBy.ID);
                    }
                    superObject objectToBeSpawned;
                    switch (object) {
                        case block -> {
    
                        }
                        case dagger -> {
                            objectToBeSpawned = object.getObject(gp, spawnedBy);
                            objectToBeSpawned.setSpawn(spawn);
                            gp.gameObjects.add(objectToBeSpawned);
                            logger(objectSpawnHEnum.objectSpawned, object, "Object Spawned, ID=" + objectToBeSpawned.ID);
                        }
                    }
                }
            }
        }
    }
}
