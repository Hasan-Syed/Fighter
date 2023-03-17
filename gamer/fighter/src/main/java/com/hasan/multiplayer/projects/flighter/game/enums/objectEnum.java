package com.hasan.multiplayer.projects.flighter.game.enums;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.dagger;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

public class objectEnum {
    enum objectType {
        miniGame("miniGame"),
        weapon("weapon"),
        none("none/Temp");

        final String name;
        private objectType (String name){
            this.name = name;
        }

        public String getName () {
            return name;
        }
    }
    public enum object {
        Rock_Paper_Scissors(2, "rock paper scissors", objectType.miniGame),
        dagger(0, "dagger", objectType.weapon),
        block(1, "block", objectType.none);
        
        final int code;
        final String name;
        final objectType type;
        private object (int code, String name, objectType type){
            this.code = code;
            this.name = name;
            this.type = type;
        }

        public String getName(){
            return name;
        }
        public String getTypeStr(){
            return type.getName();
        }

        public superObject getObject (gamePanel gp, superPlayer spawnedBy) {
            switch (code){
                case 0 -> {
                    return new dagger(gp, spawnedBy);
                }
            }
            return null;
        }
    }
}
