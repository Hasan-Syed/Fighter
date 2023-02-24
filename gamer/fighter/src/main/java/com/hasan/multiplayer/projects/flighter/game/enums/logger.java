package com.hasan.multiplayer.projects.flighter.game.enums;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;

public class logger {
    public enum statusType {
        statusUpdate(0), 
        response(1), 
        requested(2),
        spacer(3);

        final int code;
        private statusType (int code) {
            this.code = code;
        }

        public void println (String username, Object message){
            switch (code) {
                case 0 -> {
                    System.out.println("[multiplayer][client][UN=" + username +"][status]: " + message);
                }
                case 1 -> {
                    System.out.println("[multiplayer][client][UN=" + username +"][response]: " + message);
                }
                case 2 -> {
                    System.out.println("[multiplayer][client][UN=" + username +"][requested]: " + message);
                }
                case 3 -> {
                    System.out.println();
                }
            }
        }
    }
    public enum playerDeleteHEnum {
        playerDoesntExist(0),
        playerDeleted(1),
        request(2);

        final int code;
        private playerDeleteHEnum (int code){
            this.code = code;
        }

        public void println (Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println("[multiplayer][playerDeleteHandler][error=playerDoesn'tExist]: " + message);
                }
                case 1 -> {
                    System.out.println("[multiplayer][playerDeleteHandler][succressful=playerDeleted]: " + message);
                }
                case 3 -> {
                    System.out.println("[multiplayer][playerDeleteHandler][request]: " + message);
                }
            }
        }
    }
    public enum playerSpawnHEnum {
        playerAlreadyExists(0),
        playerSpawned(1);

        final int code;
        private playerSpawnHEnum (int code) {
            this.code = code;
        }

        public void println (Object message) {
            switch (code){
                case 0 -> {
                    System.out.println("[multiplayer][playerSpawnHandler][error=playerAlreadyExists]: " + message);
                }
                case 1 -> {
                    System.out.println("[multiplayer][playerSpawnHandler][succressful=playerSpawned]: " + message);
                }
            }
        }
    }
    public enum playerUpdateHEnum {
        playerNotFound(0);

        final int code;
        private playerUpdateHEnum(int code) {
            this.code = code;
        }

        public void println(int clientID, Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println("[multiplayer][playerUpdateHandler][error=playerNotFound]: " + message);
                }
            }
        }
    }
    public enum objectSpawnHEnum {
        objectAlreadyExists(0),
        objectSpawned(1),
        objectProcessing(2);

        final int code;
        private objectSpawnHEnum (int code){
            this.code = code;
        }
        public void println (object object, Object message) {
            switch (code){
                case 0 -> {
                    System.out.println("[multiplayer][objectSpawnHandler][error=objectAlreadyExists][objectName="+object.getName()+"][objectType="+object.getTypeStr()+"]: " + message);
                }
                case 1 -> {
                    System.out.println("[multiplayer][objectSpawnHandler][succressful=objectSpawned][objectName="+object.getName()+"][objectType="+object.getTypeStr()+"]: " + message);
                }
                case 2 -> {
                    System.out.println("[multiplayer][objectSpawnHandler][succressful=objectSpawned][objectName="+object.getName()+"][objectType="+object.getTypeStr()+"]: " + "processing...");
                }
            }
        }
    }
}
