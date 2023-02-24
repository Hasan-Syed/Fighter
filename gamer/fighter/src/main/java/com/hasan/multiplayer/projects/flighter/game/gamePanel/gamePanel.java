package com.hasan.multiplayer.projects.flighter.game.gamePanel;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entityTypes.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.playerTypeEntities.player;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.KeyHandler;
import com.hasan.multiplayer.projects.flighter.game.stage.stageManager;

/**
 * gamePanel is the backbone of the Game, as it is used to initialize all
 * objects (multiplayer entities exception), runs the tick that updates the
 * game, and draws the game
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class gamePanel extends JPanel implements Runnable {
    public final multiplayer multiplayer;
    // Game Main Thread
    public Thread gameThread;
    // Game Settings
    final int FPS = 60;
    // Game Loop
    public double deltaTime = 0;
    // World Settings
    final int originalTileSize = 16; // the Tile Size
    final int scale = 2; // Tile Scale Settings
    public final int tileSize = originalTileSize * scale; // Tile's scaled resolution
    public final int maxWorldCol = 26;
    public final int maxWorldRow = 13;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    // Stage manager
    public stageManager stage;
    // Controls
    public KeyHandler keyH = new KeyHandler();
    // Player
    public superPlayer player;
    public List<gameObject> gameObjects = new ArrayList<>();
    public List<superPlayer> players = new ArrayList<>();

    public gamePanel() throws UnknownHostException, IOException {
        this.setPreferredSize(new Dimension(830, 415)); // Set Screen Size
        this.setBackground(Color.BLACK); // Background Color;
        this.setDoubleBuffered(true); // Paint Setting
        this.addKeyListener(keyH); // Add Key Listener
        this.setFocusable(true);
        // Initialize Variables
        stage = new stageManager(this);
        this.addKeyListener(keyH);
        
        player = new player(this, keyH, 0);
        this.multiplayer = new multiplayer("localhost", 5050, this);
        player.ID = multiplayer.clientID;
    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        this.setName("Fighter Game Thread");
        gameThread.start();
    }

    /**
     * update() is the main game Update method, all spawned items are added here, so
     * they can be updated (if any available)
     */
    public void update() {
        multiplayer.update();
        player.update();
        players.forEach(client -> {client.update();});

        for (gameObject gameObject : gameObjects) {
            gameObject.update();
        }
    }

    /**
     * paintComponent(g) is the main draw Method, all spawned items are added here,
     * so they can be drawn
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        stage.draw(g2d.create());
        player.draw(g2d.create());
        multiplayer.draw(g2d.create());
        players.forEach(client -> {client.draw(g2d.create());});

        for (gameObject gameObject : gameObjects) {
            gameObject.draw(g);
        }
    }

    @Override
    public synchronized void run() {
        double drawInterval = 1000000000 / FPS;
        double lastTime = System.nanoTime();
        long currentTime;

        // Thread Sleep Method \\
        while (gameThread != null) {

            currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (deltaTime >= 1) {
                update(); // update gameInformation
                repaint(); // draw Updates
                deltaTime--;
            }
        }
    }
}
