package com.hasan.multiplayer.projects.flighter.game.enums;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rpsMiniGame.rps;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

public class miniGames {
    public enum games {
        rps(0);

        final int code;
        private games (int code){
            this.code = code;
        }

        public gameObject getGame (gamePanel gp){
            switch (code){
                case 0 -> {
                    return new rps(gp);
                }
                default->{
                    System.err.println("* Game Problem *");
                    return null;
                }
            }
        }
    }
}
