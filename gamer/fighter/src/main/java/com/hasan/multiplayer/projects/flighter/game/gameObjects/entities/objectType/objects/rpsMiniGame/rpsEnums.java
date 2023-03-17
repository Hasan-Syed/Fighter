package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rpsMiniGame;

public class rpsEnums {
    public enum rpsMoves {
        rock(0),
        paper(1),
        scissors(2);

        final int code;
        private rpsMoves(int code) {
            this.code = code;
        }

        public String toString (){
            switch (code){
                case 0:
                    return "rock";
                case 1:
                    return "paper";
                case 2:
                    return "scissors";
                default:
                    return "* Invalid Request *";
            }
        }
    }
}
