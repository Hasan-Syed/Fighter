package com.hasan.multiplayer.projects.flighter.game.enums;

public class sources {

    public enum source {
        // Player Handlers
        playerUpdateHandler(0),
        playerDeleteHandler(1),
        playerSpawnHandler(2),
        // Object Handlers
        objectUpdateHandler(3),
        objectDeleteHandler(4),
        objectSpawnHandler(5);

        final int code;

        private source(int code) {
            this.code = code;
        }

        public String getSource() {
            switch (code) {
                case 0:
                    return "[multiplayer][playerUpdateHandler]";
                case 1:
                    return "[multiplayer][playerDeleteHandler]";
                case 2:
                    return "[multiplayer][playerSpawnHandler]";
                case 3:
                    return "[multiplayer][objectUpdateHandler]";
                case 4:
                    return "[multiplayer][objectDeleteHandler]";
                case 5:
                    return "[multiplayer][objectSpawnHandler]";
                default:
                    return "i know this is weird but... *Invalid Source*";
            }
        }
    }
}