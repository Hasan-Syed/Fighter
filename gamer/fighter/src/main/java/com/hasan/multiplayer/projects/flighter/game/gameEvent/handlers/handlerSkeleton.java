package com.hasan.multiplayer.projects.flighter.game.gameEvent.handlers;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;

public abstract class handlerSkeleton {
    public enum possible {
        notManipulatable,
        conflict,
        goThrough;
    }
    public final int ID;
    public final String name;
    public final float incrementRate;
    public final gameObject effectedObject;
    public final boolean dualCompatable;

    public handlerSkeleton (int ID,
                            String name,
                            float incrementRate,
                            gameObject effectedObject,
                            boolean dualCompatable){
        this.ID = ID;
        this.name = name;
        this.incrementRate = incrementRate;
        this.effectedObject = effectedObject;
        this.dualCompatable = dualCompatable;
    }

    /**
     * {@code increment()} is called to perform the increment action.
     */
    public abstract void increment ();
}
