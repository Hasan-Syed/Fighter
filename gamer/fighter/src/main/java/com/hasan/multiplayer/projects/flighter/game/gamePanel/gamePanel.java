package com.hasan.multiplayer.projects.flighter.game.gamePanel;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.hasan.multiplayer.projects.flighter.game.HUD.HUD;
import com.hasan.multiplayer.projects.flighter.game.HUD.entityInfoDisplay;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;
import com.hasan.multiplayer.projects.flighter.game.HUD.notifyBar;
import com.hasan.multiplayer.projects.flighter.game.stage.stageManager;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.superObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.playerTypeEntities.player;
import com.hasan.multiplayer.projects.flighter.game.multiplayer.multiplayer;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.KeyHandler;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.mouseHandler;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.mouseMotionHandler;
import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;

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
    public final String connectionIP = "25.72.7.137";
    // HUD Management
    public HUD hud;
    public List<notifyBar> notificationList;
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
    public Dimension windowSize;
    // Stage manager
    public stageManager stage;
    public boolean displayingNotification;
    // Controls
    public KeyHandler keyH = new KeyHandler();
    public mouseMotionHandler mMotionH = new mouseMotionHandler();
    public mouseHandler mouseH = new mouseHandler();
    public Point mouseTrack = MouseInfo.getPointerInfo().getLocation();
    public Rectangle mouseHitbox = new Rectangle(0,0,0,0);
    // Player
    public superPlayer player;
    public List<superObject> gameObjects = new ArrayList<>();
    public List<superPlayer> players = new ArrayList<>();
    // draw
    public boolean drawStage = true;
    public boolean drawPlayer = true;
    public boolean drawPlayers = true;
    public boolean drawObjects = true;
    // Game Object *mini game*

    entityInfoDisplay eid;

    public gamePanel(mouseMotionHandler mMotionH, mouseHandler mouseH) throws UnknownHostException, IOException {
        windowSize = new Dimension(800, 420);
        this.setPreferredSize(windowSize); // Set Screen Size
        this.setBackground(Color.BLACK); // Background Color;
        this.setDoubleBuffered(true); // Paint Setting
        this.addKeyListener(keyH); // Add Key Listener
        this.setFocusable(true);
        this.mMotionH = mMotionH;
        this.mouseH = mouseH;
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mMotionH);
        // Initialize Variables
        hud = new HUD(this);
        stage = new stageManager(this);
        this.addKeyListener(keyH);

        player = new player(this, keyH, 0);
        this.multiplayer = new multiplayer(connectionIP, 5050, this);
        player.ID = multiplayer.clientID;

        hud.addNewMidNotification(tools.UUIDCreator(), "Welcome", 4, "[gamePanel]", notifyType.success);

        eid = new entityInfoDisplay(player, false, new Point(windowSize.width / 2, 100));
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
        mouseTrack = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseTrack, this);
        mouseHitbox.setFrameFromCenter(mouseTrack.x, mouseTrack.y, mouseTrack.x-10, mouseTrack.y-10);
        multiplayer.update();
        hud.update();
        player.update();
        players.forEach(client -> {
            client.update();
        });

        for (gameObject gameObject : gameObjects) {
            gameObject.update();
        }
        eid.update();
    }

    /**
     * paintComponent(g) is the main draw Method, all spawned items are added here,
     * so they can be drawn
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (drawStage)
            stage.draw((Graphics2D) g2d.create());
        if (drawPlayer)
            player.draw((Graphics2D) g2d.create());
        if (drawPlayers)
            players.forEach(client -> {
                client.draw((Graphics2D) g2d.create());
            });
        if (drawObjects) {
            for (gameObject gameObject : gameObjects) {
                gameObject.draw((Graphics2D) g2d.create());
            }
        }
        multiplayer.draw(g2d.create());

        hud.draw(g2d);
        g.setColor(Color.RED);
        g2d.dispose();
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
