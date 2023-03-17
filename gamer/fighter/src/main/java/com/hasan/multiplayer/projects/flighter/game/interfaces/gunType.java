package com.hasan.multiplayer.projects.flighter.game.interfaces;

public interface gunType {
    public enum status {
        notHeld(0),
        ready(1),
        onRecoil(2),
        onCooldown(3),
        initialized(4),
        initializing(5),
        reloading(6),
        reloadRequired(7);

        public String codeToString (){
            switch (code){
                case 0 :
                    return "The Weapon is not held by an entity, and not ready to be used";
                case 1 : 
                    return "The Weapon is ready to Fire";
                case 2 :
                    return "The Weapon is on Recoil, and unable to fire";
                case 3 :
                    return "The Weapon is on Cool down, and currently unable to fire";
                case 4 :
                    return "The Weapon has just Initialized";
                case 5 :
                    return "The Weapon is Initializing";
                case 6 :
                    return "The Weapon is currently Reloading";
                case 7 :
                    return "The Weapon requires a reload";
                default :
                    return "** Unimplemented Status Type **";
            }
        }

        final int code;
        private status (int code){
            this.code = code;
        }
    }
    public void fire();
    public void reload();
}
