package com.hasan.multiplayer.projects.flighter.game.stage;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStream;
import java.awt.Rectangle;
import java.io.InputStreamReader;

import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

/**
 * The Stage Manager Reads the Stage file to Create the Stage on the clients Display
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class stageManager {
    final gamePanel gp;
    List<tile> Tiles = new ArrayList<>();
    int[][] stage;
    
    public stageManager (gamePanel gp){
        this.gp = gp;
        this.stage = new int[gp.maxWorldCol][gp.maxWorldRow];
        
        loadTiles();
        loadMap("/com/hasan/multiplayer/projects/flighter/resources/stageData/stage1.txt");
    }
    // Load tile
    public void loadTiles() {
        tile tempTile = null;
        // black
        tempTile = new tile(0, 
            "black", 
            tools.loadScaledSprite(getClass(), "/com/hasan/multiplayer/projects/flighter/resources/stageResources/black.png", gp.tileSize, gp.tileSize), 
            false, 
            null);
        Tiles.add(tempTile);
        // green
        tempTile = new tile(1, 
            "grass", 
            tools.loadScaledSprite(getClass(), "/com/hasan/multiplayer/projects/flighter/resources/stageResources/grass.png", gp.tileSize, gp.tileSize), 
            true, 
            null);
        Tiles.add(tempTile);
    }
    // Level Loader
    public synchronized void loadMap(String mapLoc) {
        try {
            InputStream ir = getClass().getResourceAsStream(mapLoc); // Reader for Reading Map File
            BufferedReader br = new BufferedReader(new InputStreamReader(ir));
            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                // Fill out X- axis
                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" "); // Split the line om ' '(Space)
                    int tileID = Integer.parseInt(numbers[col]); // X- axis data
                    stage[col][row] = tileID; // Place on X- Axis
                    col++;
                }
                // Add to Y- Axis
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close(); // Close Reader
            ir.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {

    }

    public void draw(Graphics g) {
        int x = 0;
        int y = 0;
        Rectangle stageCheck;
        for (int rows = 0; rows != gp.maxWorldRow; rows++){
            x = 0;
            for (int cols = 0; cols != gp.maxWorldCol ; cols ++){
                tile used = Tiles.get(stage[cols][rows]);
                stageCheck = new Rectangle(x,y,gp.tileSize, gp.tileSize);

                g.drawImage(used.tile, x, y,null, gp);
                hitboxCheck(used, stageCheck);
                g.setColor(Color.GRAY);

                x += gp.tileSize;
            }

            y += gp.tileSize;
        }
    }

    void hitboxCheck (tile used, Rectangle stageCheck) {
        if (stageCheck.intersects(gp.player.hitbox)&& used.collidable == true) {
            // There is a collision
            // Get the overlapping rectangle
            Rectangle overlap = stageCheck.intersection(gp.player.hitbox);
            // Check which side the collision is happening
            if (overlap.width > overlap.height) {
              // The collision is happening on the top or bottom
              if (stageCheck.y < gp.player.hitbox.y) {
                // The collision is happening on the bottom
                gp.player.worldPosition.y = gp.player.hitbox.y = stageCheck.y - gp.player.hitbox.height;
              } else {
                // The collision is happening on the top
                gp.player.worldPosition.y = gp.player.hitbox.y = stageCheck.y - gp.player.hitbox.height;
              }
            } else {
              // The collision is happening on the left or right
              if (stageCheck.x < gp.player.hitbox.x) {
                // The collision is happening on the right
              } else {
                // The collision is happening on the left
              }
            }
        }
    }

    void playerCheck () {

    }
}
