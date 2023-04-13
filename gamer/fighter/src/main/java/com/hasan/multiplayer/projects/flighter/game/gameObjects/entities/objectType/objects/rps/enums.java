package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps;

public class enums {
    public enum selectedRPS {
        rock(0),
        paper(1),
        scissors(2),
        none(3);

        final int code;
        private selectedRPS (int code) {
            this.code = code;
        }

        public int code(){
            return code;
        }
    }
    public enum winCheck {
        won,
        tie,
        loss;
    }
}
