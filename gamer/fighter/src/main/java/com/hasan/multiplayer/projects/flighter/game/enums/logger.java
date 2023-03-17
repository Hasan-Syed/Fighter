package com.hasan.multiplayer.projects.flighter.game.enums;

import com.hasan.multiplayer.projects.flighter.game.enums.objectEnum.object;
import com.hasan.multiplayer.projects.flighter.game.enums.sources.source;

public class logger {
    public enum statusType {
        statusUpdate(0),
        response(1),
        requested(2),
        spacer(3);

        final int code;

        private statusType(int code) {
            this.code = code;
        }

        public void println(String username, Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println("[multiplayer][client][UN=" + username + "][status]: " + message);
                }
                case 1 -> {
                    System.out.println("[multiplayer][client][UN=" + username + "][response]: " + message);
                }
                case 2 -> {
                    System.out.println("[multiplayer][client][UN=" + username + "][requested]: " + message);
                }
                case 3 -> {
                    System.out.println();
                }
            }
        }
    }

    /**
     * Handle Logs for playerDeletes, sent from the server
     * 
     * @version 1.5
     * @since 1.0
     */
    public enum playerDeleteHEnum {
        playerDoesntExist(0),
        playerDeleted(1),
        request(2);

        final source source = sources.source.playerDeleteHandler;
        final int code;

        private playerDeleteHEnum(int code) {
            this.code = code;
        }

        public void println(Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println(source.getSource() + "[error=playerDoesn'tExist]: " + message);
                }
                case 1 -> {
                    System.out.println(source.getSource() + "[succressful=playerDeleted]: " + message);
                }
                case 3 -> {
                    System.out.println(source.getSource() + "[request]: " + message);
                }
            }
        }
    }

    /**
     * Handle Logs for playerSpawn, sent from the server
     * 
     * @version 1.5
     * @since 1.0
     */
    public enum playerSpawnHEnum {
        playerAlreadyExists(0),
        playerSpawned(1);

        final source source = sources.source.playerSpawnHandler;
        final int code;

        private playerSpawnHEnum(int code) {
            this.code = code;
        }

        public void println(Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println(source.getSource() + "[error=playerAlreadyExists]: " + message);
                }
                case 1 -> {
                    System.out.println(source.getSource() + "[succressful=playerSpawned]: " + message);
                }
            }
        }
    }

    /**
     * Handle Logs for playerUpdate, sent from the server
     * 
     * @version 1.5
     * @since 1.0
     */
    public enum playerUpdateHEnum {
        playerNotFound(0);

        final source source = sources.source.playerDeleteHandler;
        final int code;

        private playerUpdateHEnum(int code) {
            this.code = code;
        }

        public void println(int clientID, Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println(source.getSource() + "[error=playerNotFound]: " + message);
                }
            }
        }
    }
    /**
    * Handles Logs for objectSpawn, sent from the server
    * @version 1.5
    * @since 1.0
    */
    public enum objectSpawnHEnum {
        objectAlreadyExists(0),
        objectSpawned(1),
        objectProcessing(2);

        final source source = sources.source.objectSpawnHandler;
        final int code;

        private objectSpawnHEnum(int code) {
            this.code = code;
        }

        public void println(object object, Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println(source.getSource() + "[error=objectAlreadyExists][objectName="
                            + object.getName() + "][objectType=" + object.getTypeStr() + "]: " + message);
                }
                case 1 -> {
                    System.out.println(source.getSource() + "[succressful=objectSpawned][objectName="
                            + object.getName() + "][objectType=" + object.getTypeStr() + "]: " + message);
                }
                case 2 -> {
                    System.out.println(source.getSource() + "[processing][objectName=" + object.getName()
                            + "][objectType=" + object.getTypeStr() + "]: " + message);
                }
            }
        }
    }
    /**
    * Handles Logs for objectDelete, sent from the server
    * @version 1.5
    * @since 1.0
    */
    public enum objectDeleteHEnum {
        objectDoesntExist(0),
        objectDeleted(1),
        objectProcessing(2);

        final source source = sources.source.objectDeleteHandler;
        final int code;

        private objectDeleteHEnum(int code) {
            this.code = code;
        }

        public void println(Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println(source.getSource() + "[error=objectDoesn'tExist]: " + message);
                }
                case 1 -> {
                    System.out.println(source.getSource() + "[succressful=objectDeleted]: " + message);
                }
                case 2 -> {
                    System.out.println(source.getSource() + "[processing]: " + message);
                }
            }
        }
    }
}
