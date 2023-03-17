package com.hasan.multiplayer.projects.flighter.game.gameObjects.enums;

public enum gameObjectType {
    player(0),
    object(1),
    minigame(2);

    final int code;

    private gameObjectType(int code) {
        this.code = code;
    }
}