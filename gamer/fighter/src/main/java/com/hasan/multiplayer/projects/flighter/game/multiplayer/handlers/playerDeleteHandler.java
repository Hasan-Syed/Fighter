package com.hasan.multiplayer.projects.flighter.game.multiplayer.handlers;

import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.enums.logger.playerDeleteHEnum;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entityTypes.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;

public class playerDeleteHandler {
    final gamePanel gp;
    final multiplayer multiplayer;

    public playerDeleteHandler(gamePanel gp, multiplayer multiplayer) {
        this.gp = gp;
        this.multiplayer = multiplayer;
    }

    void logger(playerDeleteHEnum logType, Object message) {
        logType.println(message);
    }

    public void playerDelete(JSONArray playerDelete) {
        JSONObject delete;
        for (int index = 0; index != playerDelete.length(); index++) {
            delete = playerDelete.getJSONObject(index);
            // Get the Delete ID
            final int deleteID = delete.getInt("deleteSpawnID");
            logger(playerDeleteHEnum.request, "ID="+ deleteID + ", has been requested to be deleted");
            // Try to find if the player exists
            // Try to Reduce down to one Player Object
            try {
                for (int indexDelete = 0; indexDelete < gp.players.size(); indexDelete++){
                    if (gp.players.get(indexDelete).ID == deleteID){
                        superPlayer temp = gp.players.get(indexDelete);
                        gp.players.remove(indexDelete);
                        logger(playerDeleteHEnum.playerDeleted, "UN=" + temp.name + ", ID="+ deleteID + ", has been Deleted");
                    }
                }
            } catch (NoSuchElementException playerDoesntExist) {
                logger(playerDeleteHEnum.playerDoesntExist, "ID="+ deleteID + ", doesn't exist");
            }
        }
    }
}
