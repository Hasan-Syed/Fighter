package com.hasan.multiplayer.projects.flighter.game.stage;

import java.awt.Image;

import com.hasan.multiplayer.projects.flighter.game.enums.stageEnums.collisionPenalty;

/**
 * Tiles are Items that the set around the screen, to make obsticles and the stage
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class tile {
    public final int id;
    public final String name;
    public final Image tile;
    public final boolean collidable;
    public final collisionPenalty collisionPenalty;

    /**
     * @param id
     * @param name
     * @param tile
     * @param collidable
     * @param collisionPenalty
     */
    public tile (int id, String name, Image tile, boolean collidable, collisionPenalty collisionPenalty){
        this.id = id;
        this.name = name;
        this.tile = tile;
        this.collidable = collidable;
        this.collisionPenalty = collisionPenalty;
    }
}
