package com.hasan.multiplayer.projects.flighter.game.enums;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.object.dagger;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.objectTypes.superObject;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

public class objectEnum {
    enum objectType {
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

        public superObject getObject (gamePanel gp) {
            switch (code){
                case 0 -> {
                    return new dagger(gp);
                }
            }
            return null;
        }
    }
}
