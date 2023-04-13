package com.hasan.multiplayer.projects.fighter.server.enums;

public class logger {
    public enum serverType {
        mini("miniServer"),
        main("mainServer");

        final String code;

        private serverType(String code) {
            this.code = code;
        }

        public String serverTypeTag() {
            switch (code) {
                case "miniServer":
                    return "[miniServer]";
                case "mainServer":
                    return "[mainServer]";
                default:
                    return "[error]";
            }
        }
    }

    public enum serverLogger {
        statusUpdate(0),
        initializePhase(1),
        spacer(2);

        final int code;

        private serverLogger(int code) {
            this.code = code;
        }

        public void println(serverType type, String serverName, Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println(
                            "[Server]" + type.serverTypeTag() + "[" + serverName + "][statusUpdate]: " + message);
                }
                case 1 -> {
                    System.out.println(
                            "[Server]" + type.serverTypeTag() + "[" + serverName + "][initialzePhaze]: " + message);
                }
                case 2 -> {
                    System.out.println();
                }
            }
        }
    }

    public enum miniGameStatus {
        statusUpdate(0),
        response(1),
        requested(2),
        spacer(3),
        error(4);

        final int code;

        private miniGameStatus(int code) {
            this.code = code;
        }

        public void println(int clientID, String name, Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println(
                            "[multiplayer][miniGame=" + name + "][client][ID=" + clientID + "][status]: " + message);
                }
                case 1 -> {
                    System.out.println(
                            "[multiplayer][miniGame=" + name + "][client][ID=" + clientID + "][response]: " + message);
                }
                case 2 -> {
                    System.out.println(
                            "[multiplayer][miniGame=" + name + "][client][ID=" + clientID + "][requested]: " + message);
                }
                case 3 -> {
                    System.out.println();
                }
                case 4 -> {
                    System.out.println(
                            "[multiplayer][miniGame=" + name + "][client][ID=" + clientID + "][Error]: " + message);
                }
            }
        }
    }

    public enum statusType {
        statusUpdate(0),
        response(1),
        requested(2),
        spacer(3),
        error(4);

        final int code;

        private statusType(int code) {
            this.code = code;
        }

        public void println(int clientID, Object message) {
            switch (code) {
                case 0 -> {
                    System.out.println("[multiplayer][client][ID=" + clientID + "][status]: " + message);
                }
                case 1 -> {
                    System.out.println("[multiplayer][client][ID=" + clientID + "][response]: " + message);
                }
                case 2 -> {
                    System.out.println("[multiplayer][client][ID=" + clientID + "][requested]: " + message);
                }
                case 3 -> {
                    System.out.println();
                }
                case 4 -> {
                    System.out.println("[multiplayer][client][ID=" + clientID + "][Error]: " + message);
                }
            }
        }
    }
}
