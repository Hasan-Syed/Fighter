package com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;
import com.hasan.multiplayer.projects.flighter.game.enums.logger.playerSpawnHEnum;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.playerTypeEntities.remotePlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

/**
 * playerUpdateHandler, used to spawn player-type entities on the client
 * 
 * @author Hasan Syed
 * @since 1.0
 * @version 1.0
 */
public class playerSpawnHandler {
    final gamePanel gp;
    final multiplayer multiplayer;

    public playerSpawnHandler(gamePanel gp, multiplayer multiplayer) {
        this.gp = gp;
        this.multiplayer = multiplayer;
    }

    void logger(playerSpawnHEnum logType, Object message) {
        logType.println(message);
    }

    public void playerSpawn(JSONArray playerSpawns) {
        JSONObject spawn;
        for (int index = 0; index != playerSpawns.length(); index++) {
            spawn = playerSpawns.getJSONObject(index);
            // Check if the Player Exists
            final int playerID = spawn.getInt("ID");
            // Check if the player Exists
            boolean playerExists = gp.players.stream().filter((user) -> user.ID == playerID)
                    .findFirst().isPresent();

            if (playerExists){
                superPlayer player = gp.players.stream().filter((user) -> user.ID == playerID)
                    .findFirst().get();
                logger(playerSpawnHEnum.playerAlreadyExists, "UN=" + player.name + ", ID=" + player.ID + ", currentPosition=" + player.worldPosition);
            } else {
                spawn = playerSpawns.getJSONObject(index);
                remotePlayer newPlayer = new remotePlayer(gp, true, spawn);
                gp.players.add(newPlayer);
                gp.hud.addNewMidNotification(tools.UUIDCreator(), newPlayer.name + " has joined", 3, "[multiplayer][playerSpawnHandler]", notifyType.success);
                logger(playerSpawnHEnum.playerSpawned, "UN=" + newPlayer.name + " ID=" + newPlayer.ID);
            }
        }
    }
}
