package com.hasan.multiplayer.projects.flighter.game.gameEvent.handlers;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;

public class healthHandler extends handlerSkeleton {

    public healthHandler(int ID,
            String name,
            int incrementRate,
            gameObject effectedObject,
            boolean dualCompatable) {
        super(ID, name, incrementRate, effectedObject, dualCompatable);
    }

    public void increment() {
        possible check = possible();
        switch (check) {
            case conflict:
                break;
            case goThrough:
                effectedObject.cHealth += incrementRate;
                break;
            case notManipulatable:
                break;
        }
    }

    possible possible() {
        // Checks if the health is manipulatable at all
        if (effectedObject.healthManipulatable) {
            // checks if the addition of health will cause any conflicts with max health
            if ((effectedObject.cHealth += incrementRate) <= effectedObject.maxHealth)
                return possible.goThrough; // No conflicts
            else
                return possible.conflict; // Conflict found
        } else {
            return possible.notManipulatable; // Health is not Manipulatable
        }
    }
}
