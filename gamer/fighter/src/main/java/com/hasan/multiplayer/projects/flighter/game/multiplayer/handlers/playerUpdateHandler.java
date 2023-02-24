package com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers;

import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.enums.logger.playerUpdateHEnum;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entityTypes.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;

public class playerUpdateHandler {
    final gamePanel gp;
    final multiplayer multiplayer;

    public playerUpdateHandler(gamePanel gp, multiplayer multiplayer) {
        this.gp = gp;
        this.multiplayer = multiplayer;
    }

    void logger(playerUpdateHEnum logType, int clientID, String message) {
        logType.println(clientID, message);
    }

    public void playerUpdate(JSONArray playerUpdates) {
        JSONObject update;
        for (int index = 0; index != playerUpdates.length(); index++) {
            try {
                update = playerUpdates.getJSONObject(index);
                // Check if the Player Exists
                final int playerID = update.getInt("ID");
                // Try to find if the player exists
                // Try to Reduce down to one Player Object
                superPlayer player = gp.players.stream().filter((user) -> user.ID == playerID)
                        .findFirst().get();
                // Apply the Update
                player.setPlayerUpdate(update);
            } catch (NoSuchElementException playerDoesntExist) {
                update = playerUpdates.getJSONObject(index);
                // Check if the Player Exists
                final int playerID = update.getInt("ID");
                logger(playerUpdateHEnum.playerNotFound, playerID, "player not Found");
            } catch (JSONException nullError) {
                System.err.println(nullError);
            }
        }
    }
}
