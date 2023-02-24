package com.hasan.multiplayer.projects.fighter.server.enums;

public class logger {
    public enum statusType {
        statusUpdate(0), 
        response(1), 
        requested(2),
        spacer(3),
        error(4);

        final int code;
        private statusType (int code) {
            this.code = code;
        }

        public void println (int clientID, Object message){
            switch (code) {
                case 0 -> {
                    System.out.println("[multiplayer][client][ID=" + clientID +"][status]: " + message);
                }
                case 1 -> {
                    System.out.println("[multiplayer][client][ID=" + clientID +"][response]: " + message);
                }
                case 2 -> {
                    System.out.println("[multiplayer][client][ID=" + clientID +"][requested]: " + message);
                }
                case 3 -> {
                    System.out.println();
                }
                case 4 -> {
                    System.out.println("[multiplayer][client][ID=" + clientID +"][Error]: " + message);
                }
            }
        }
    }
}
