package com.hasan.multiplayer.projects.flighter.game.gameEvent;

public class enums {
    public enum gameEventType {
        health(0),
        speed(1);

        final int code;

        private gameEventType (int code){
            this.code = code;
        }
    }
}