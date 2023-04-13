package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.DimensionUIResource;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;
import com.hasan.multiplayer.projects.flighter.game.enums.multiplayer.serverMessageReturnType;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.mouseHandler;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.winCheck;

public class rpsBackWorks {
    // Selection Process
    protected boolean iHaveSelected;
    protected boolean sendReady;
    protected selectedRPS selectedRPS = com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none;
    // Game Req Ops
    protected final gamePanel gp;
    protected final rps rpsObject;
    protected final rpsGameLobby rpsLobby;
    // Game Settings
    protected final Point windowStartPoint;
    protected final Dimension gameWindowSize;
    protected final Dimension gameWindowOffset;
    protected final Dimension gamePlayerWindow;
    // Game Controls
    protected final mouseHandler mouseH;
    // Game Player
    protected List<superPlayer> connectedPlayers;
    // Mouse hitbox Hitbox/Correction
    protected int mouseHitboxX, mouseHitboxY, mouseHitboxSize;
    protected Rectangle mouseHitbox = new Rectangle(0, 0, 0, 0);
    // Game Assets
    private List<Image> unSelectedIconScale;
    private List<Image> selectedIconScale;
    private List<Rectangle> iconHitboxes;
    // Asset Positioning
    private List<Point> unSelectedIconsPositions;
    // Multiplayer Queues
    boolean queueDisconnect = false;
    JSONObject requestsToServer;
    // Multiplayer Settings
    private boolean playerStaisfied = false;
    // opponent Settings
    private superPlayer opponent;
    private boolean opponentHidden;
    private boolean opponentRecived;
    private selectedRPS opponentSelection;

    /**
     * {@code rpsBackworks()} is used to draw the mini game on the screen,
     * {@code rps()} object is used to process (Send and Recieve online, data) any
     * and all the data produced by {@code rpsBackworks()} (this).
     * 
     * @param gp
     */
    rpsBackWorks(gamePanel gp, rps rpsObject) {
        // Game Panel
        this.gp = gp;
        this.rpsObject = rpsObject;
        this.rpsLobby = rpsObject.rpsGameLobby;
        this.mouseH = gp.mouseH;
        // The Mini-Game's Offset from the main Game Window
        this.gameWindowOffset = new Dimension(30, 20);
        // Temporary Dimension Hold of the Main Game Window Size
        Dimension temp = gp.windowSize;
        // Initializes and Sets the Game Window Size, These values can change if the
        // main Game Window is resized
        this.gameWindowSize = new Dimension(
                temp.width - gameWindowOffset.width,
                temp.height - gameWindowOffset.height);
        // Initialize the Game Draw Point Position, these values can change if the main
        // Game window is resized
        this.windowStartPoint = new Point(
                (temp.width / 2) - (gameWindowSize.width / 2),
                (temp.height / 2) - (gameWindowSize.height / 2));
        this.gamePlayerWindow = new Dimension(
                gameWindowSize.height - 80,
                ((gameWindowSize.width) / 2) - 10);
        // Initialize Connected Player
        connectedPlayers = new ArrayList<>(2);
        // Initialize Assets
        initializeAssets();
    }

    void initializeAssets() {
        iHaveSelected = false;
        sendReady = false;
        opponentRecived = false;
        iconHitboxes = new ArrayList<>();
        unSelectedIconsPositions = new ArrayList<>();
        List<String> temp = new ArrayList<>(3);
        temp.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/assets/rock.png");
        temp.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/assets/paper.png");
        temp.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/assets/scissors.png");
        temp.add("/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/assets/unknown.png");
        // Setup unselected Icons
        {
            // Initialize Unselected and load Icons
            unSelectedIconScale = tools.loadScaledSprite(getClass(), temp, 45, 45);
            unSelectedIconScale.set(1, tools.loadScaledSprite(getClass(), temp.get(1), 50, 45));
            unSelectedIconScale.set(2, tools.loadScaledSprite(getClass(), temp.get(2), 50, 45));
        }
        // Setup selected Icons
        {
            // Initialize selected and load Icons
            selectedIconScale = tools.loadScaledSprite(getClass(), temp, 150, 150);
            selectedIconScale.set(0, tools.loadScaledSprite(getClass(), temp.get(0), 134, 157));
            selectedIconScale.set(1, tools.loadScaledSprite(getClass(), temp.get(1), 164, 169));
            selectedIconScale.set(2, tools.loadScaledSprite(getClass(), temp.get(2), 226, 160));
        }
        opponentHidden = true;
        opponentSelection = com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none;
    }

    public JSONObject whatAmI() {
        return (new JSONObject()
                .put("miniName", rpsObject.name)
                .put("requestType", "whatAmI")
                .put("selection", selectedRPS));
    }

    public winCheck checkMyWinCondition() {
        if (selectedRPS == opponentSelection) {
            return winCheck.tie;
        } else if (selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.rock
                &&
                opponentSelection == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.scissors
                ||
                selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.scissors
                        &&
                        opponentSelection == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.paper
                ||
                selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.paper
                        &&
                        opponentSelection == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.rock) {
            return winCheck.won;
        } else {
            return winCheck.loss;
        }
    }

    public void update() {
        mouseHitboxSize = 2;
        if (connectedPlayers.size() < 2) {
            playerStaisfied = false;
        }
        // Update Game Window Settings
        {
            // Temporary Dimension Hold of the Main Game Window Size
            Dimension temp = gp.windowSize;
            // updates and Sets the Game Window Size, These values can change if the
            // main Game Window is resized
            gameWindowSize.setSize(
                    temp.width - gameWindowOffset.width,
                    temp.height - gameWindowOffset.height);
            // updates the Game Draw Point Position, these values can change if the main
            // Game window is resized
            windowStartPoint.setLocation(
                    (temp.width / 2) - (gameWindowSize.width / 2),
                    (temp.height / 2) - (gameWindowSize.height / 2));
        }
        // Wait for second player
        if (rpsObject.rpsGameLobby != null && rpsObject.rpsGameLobby.isConnected()) {
            requestsToServer = new JSONObject();
            // Write server Requests
            // If players are not satisfying the lobby request
            if (!playerStaisfied) {
                requestsToServer.put("lobbySatisfy", false);
            }
            // If my player disconnects
            if (queueDisconnect) {
                requestsToServer.put("disconnectMe", true);
            }
            // I have Selected
            if (sendReady) {
                requestsToServer.put("gameData", whatAmI());
                sendReady = false;
            }
            // Send Requests to the server
            rpsObject.rpsGameLobby.writeServer(requestsToServer);
            // Read Server Response
            JSONArray serverResponse = (JSONArray) rpsObject.rpsGameLobby.readServer(serverMessageReturnType.jsonArray);
            for (Object followThroughOBJ : serverResponse) {
                JSONObject followThrough = (JSONObject) followThroughOBJ;
                String requestType = followThrough.getString("requestType");
                // use switch-case to get to the correct requestType
                switch (requestType) {
                    case "satisfy" -> {
                        JSONArray connectedEntities = followThrough.getJSONArray("connectedEntities");
                        for (Object clientIDOBJ : connectedEntities) {
                            int clientID = (int) clientIDOBJ;
                            if (clientID != gp.multiplayer.clientID) {
                                // Add player to connected Players list
                                tools.addPlayerMini(connectedPlayers, tools.findByID(gp.players, clientID));
                            }
                        }
                    }
                    case "disconnectClient" -> {
                        int disconnectID = followThrough.getJSONObject("clientInfo").getInt("ID");
                        // remove player from connected players list
                        tools.removePlayerMini(connectedPlayers, tools.findByID(gp.players, disconnectID));
                        playerStaisfied = false;
                    }
                    case "whatAmI" -> {
                        opponentSelection = followThrough.getEnum(
                                com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.class,
                                "selection");
                        opponentRecived = true;
                        opponentHidden = true;
                    }
                }
                if (queueDisconnect) {
                    rpsObject.rpsGameLobby = null;
                    rpsObject.backWorks = new rpsBackWorks(gp, rpsObject);
                }
            }
        }
        // Game Mech
        {
            // Expose to players
            {
                if (iHaveSelected && opponentRecived) {
                    opponentHidden = false;
                    winCheck wc = checkMyWinCondition();
                    switch (wc) {
                        case loss -> {
                            gp.hud.addNewTopNotification(69, wc.toString(), 3, "[rps][rpsBackWorks]", notifyType.info);
                        }
                        case tie -> {
                            gp.hud.addNewTopNotification(69, wc.toString(), 3, "[rps][rpsBackWorks]", notifyType.info);
                        }
                        case won -> {
                            gp.hud.addNewTopNotification(69, wc.toString(), 3, "[rps][rpsBackWorks]", notifyType.info);
                        }
                    }
                }
            }
        }
    }

    public void draw(Graphics2D g2d) {
        Graphics2D gameBoard = (Graphics2D) g2d.create(windowStartPoint.x, windowStartPoint.y, gameWindowSize.width,
                gameWindowSize.height);
        drawGameBoard(gameBoard);
        mouseHitboxX = gp.mMotionH.mouseLoc.x - windowStartPoint.x;
        mouseHitboxY = gp.mMotionH.mouseLoc.y - windowStartPoint.y;
        mouseHitbox.setFrameFromCenter(mouseHitboxX, mouseHitboxY, mouseHitboxX - mouseHitboxSize,
                mouseHitboxY - mouseHitboxSize);
        gameBoard.draw(mouseHitbox);
        gameBoard.dispose();
        g2d.dispose();
    }

    /**
     * {@code drawGameBoard()} is used to draw the Game Board
     * 
     * @param gameBoard Graphics Object
     */
    private void drawGameBoard(Graphics2D gameBoard) {
        // Draw Back ground
        gameBoard.fill3DRect(0, 0, gameWindowSize.width, gameWindowSize.height, true);
        // Draw In Game Text
        gameBoard.setFont(new Font("Arial", Font.BOLD, 50));
        gameBoard.setColor(Color.white);
        gameBoard.drawString("RPS", 8, 50);
        // Draw Game ID
        gameBoard.setFont(new Font("Arial", Font.BOLD, 10));
        gameBoard.setColor(Color.white);
        gameBoard.drawString("GameID: " + rpsObject.ID, 13, 65);

        int newHeight = gameWindowSize.height - 80;
        int newW = ((gameWindowSize.width) / 2) - 10;

        boolean drawFirstPlayer = false;
        boolean drawSecondPlayers = false;
        if (connectedPlayers.size() == 2) {
            while (!drawFirstPlayer && !drawSecondPlayers) {
                if (connectedPlayers.get(0).ID == gp.multiplayer.clientID) {
                    drawPlayerBox(gameBoard, new Point(10, 70), new DimensionUIResource(newW, newHeight),
                            connectedPlayers.get(0));
                    drawSecondPlayers = true;
                } else {
                    drawPlayerBox(gameBoard, new Point(newW + 10, 70), new DimensionUIResource(newW, newHeight),
                            connectedPlayers.get(0));
                    drawFirstPlayer = true;
                }

                if (connectedPlayers.get(1).ID == gp.multiplayer.clientID) {
                    drawPlayerBox(gameBoard, new Point(10, 70), new DimensionUIResource(newW, newHeight),
                            connectedPlayers.get(1));
                    drawSecondPlayers = true;
                    // Correct mouse Position
                    {
                        mouseHitboxX -= 10;
                        mouseHitboxY -= 70;
                        mouseHitbox.setFrameFromCenter(mouseHitboxX, mouseHitboxY,
                                (mouseHitboxX - 10) - mouseHitboxSize,
                                (mouseHitboxY - 70) - mouseHitboxSize);
                    }
                } else {
                    drawPlayerBox(gameBoard, new Point(newW + 10, 70), new DimensionUIResource(newW, newHeight),
                            connectedPlayers.get(1));
                    drawFirstPlayer = true;
                }
            }
        }

    }

    private void drawPlayerBox(Graphics2D gameBoard, Point position, Dimension dimension, superPlayer player) {
        dimension.height = dimension.height;
        dimension.width = dimension.width;
        Graphics2D playerBoard = (Graphics2D) gameBoard.create(position.x, position.y, dimension.width + 1,
                dimension.height + 1);
        // Draw Player Rect
        playerBoard.drawRect(0, 0, dimension.width, dimension.height);
        mouseHitbox.setFrameFromCenter(mouseHitboxX - position.x, mouseHitboxY - position.y,
                (mouseHitboxX - position.x) - mouseHitboxSize, (mouseHitboxY - position.y) - mouseHitboxSize);
        // Draw Player Name
        playerBoard.setFont(new Font("arial", Font.BOLD, 24));
        playerBoard.drawString(player.name, 8, 25);
        // Draw selected Area
        int offset = 30;
        // Draw Selected Icon
        {
            // If the currently Drawing PlayerBox is me or Other
            if (player.ID == gp.multiplayer.clientID) {
                // Calculate Position/Dimensions
                int drawX = (dimension.width / 2) - selectedIconScale.get(selectedRPS.code).getWidth(gp) / 2;
                int drawY = (dimension.height / 2) - 75;
                // If i have selected
                if (iHaveSelected) {
                    // I have selected, show the Selected Image
                    playerBoard.drawImage(
                            selectedIconScale.get(selectedRPS.code),
                            drawX,
                            drawY - offset,
                            gp);
                } else {
                    // I have selected, show the Selected Image
                    playerBoard.drawImage(
                            selectedIconScale.get(
                                    com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none.code),
                            drawX,
                            drawY - offset,
                            gp);
                }
            } else {
                if (opponentHidden) {
                    playerBoard.drawImage(
                            selectedIconScale.get(
                                    com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none.code),
                            dimension.width / 2 - 75,
                            (dimension.height / 2 - 75) - offset,
                            gp);
                } else {
                    // Calculate Position/Dimensions
                    int drawX = (dimension.width / 2) - selectedIconScale.get(opponentSelection.code).getWidth(gp) / 2;
                    int drawY = (dimension.height / 2) - 75;

                    // Opponent has selected, show the Selected Image
                    playerBoard.drawImage(
                            selectedIconScale.get(opponentSelection.code),
                            drawX,
                            drawY - offset,
                            gp);
                }
            }
        }
        // Draw
        int lineOffSet = 25;
        {
            unSelectedIconsPositions = new ArrayList<>();
            unSelectedIconsPositions
                    .add(new Point(dimension.width / 2 - unSelectedIconScale.get(0).getWidth(gp) / 2 - lineOffSet
                            - unSelectedIconScale.get(0).getWidth(gp), (dimension.height / 2) + (150 / 2)));
            unSelectedIconsPositions.add(new Point(dimension.width / 2 - unSelectedIconScale.get(1).getWidth(gp) / 2,
                    (dimension.height / 2) + (150 / 2)));
            unSelectedIconsPositions
                    .add(new Point(dimension.width / 2 + unSelectedIconScale.get(2).getWidth(gp) / 2 + lineOffSet,
                            (dimension.height / 2) + (150 / 2)));
        }
        // Primary Player, add Clickable Buttons
        if (player.ID == gp.multiplayer.clientID) {
            icon_Placement_Hitboxes(playerBoard);
            playerControlsDetections();
        }
        // This is not the primary player, add unclickanble Buttons
        else {
            playerBoard.drawImage(unSelectedIconScale.get(0), unSelectedIconsPositions.get(0).x,
                    unSelectedIconsPositions.get(0).y, gp);
            playerBoard.drawImage(unSelectedIconScale.get(1), unSelectedIconsPositions.get(1).x,
                    unSelectedIconsPositions.get(1).y, gp);
            playerBoard.drawImage(unSelectedIconScale.get(2), unSelectedIconsPositions.get(2).x,
                    unSelectedIconsPositions.get(2).y, gp);
        }
        // Dispose
        playerBoard.dispose();
    }

    /**
     * {@code icon_Placement_Hitboxes()} is used to place game icons and hitboxes on
     * the game board.
     * 
     * @param playerBoard gameBoard
     */
    private void icon_Placement_Hitboxes(Graphics2D playerBoard) {
        // Default Alpha Composite
        Composite compositeDef = playerBoard.getComposite();
        // Reset IconHitboxes
        iconHitboxes = new ArrayList<>();
        {
            // Draw Rock
            iconHitboxes.add(new Rectangle(
                    unSelectedIconsPositions.get(0).x,
                    unSelectedIconsPositions.get(0).y,
                    unSelectedIconScale.get(0).getWidth(gp),
                    unSelectedIconScale.get(0).getHeight(gp)));
            // Set Composite
            if (iHaveSelected &&
                    selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.rock
                    ||
                    selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none) {
                // Leave as Default
                playerBoard.setComposite(compositeDef);
            } else {
                // set the opacity
                playerBoard.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            // Draw Rock
            playerBoard.drawImage(unSelectedIconScale.get(
                    com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.rock.code),
                    unSelectedIconsPositions.get(0).x,
                    unSelectedIconsPositions.get(0).y, gp);
        }
        {
            // Draw Paper
            iconHitboxes.add(new Rectangle(
                    unSelectedIconsPositions.get(1).x,
                    unSelectedIconsPositions.get(1).y,
                    unSelectedIconScale.get(1).getWidth(gp),
                    unSelectedIconScale.get(1).getHeight(gp)));
            // Set Composite
            if (iHaveSelected &&
                    selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.paper
                    ||
                    selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none) {
                // Leave as Default
                playerBoard.setComposite(compositeDef);
            } else {
                // set the opacity
                playerBoard.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            // Draw Paper
            playerBoard.drawImage(unSelectedIconScale.get(
                    com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.paper.code),
                    unSelectedIconsPositions.get(1).x,
                    unSelectedIconsPositions.get(1).y, gp);
        }
        {
            // Draw Scissors
            iconHitboxes.add(new Rectangle(
                    unSelectedIconsPositions.get(2).x,
                    unSelectedIconsPositions.get(2).y,
                    unSelectedIconScale.get(2).getWidth(gp),
                    unSelectedIconScale.get(2).getHeight(gp)));
            // Set Composite
            if (iHaveSelected &&
                    selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.scissors
                    ||
                    selectedRPS == com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none) {
                // Leave as Default
                playerBoard.setComposite(compositeDef);
            } else {
                // set the opacity
                playerBoard.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            // Draw Scissors
            playerBoard.drawImage(unSelectedIconScale.get(
                    com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.scissors.code),
                    unSelectedIconsPositions.get(2).x,
                    unSelectedIconsPositions.get(2).y, gp);
        }
    }

    /**
     * {@code playerControlsDetections()} is used to detect player mouse movements
     * and act upon them.
     */
    private void playerControlsDetections() {
        if (!mouseHitbox.intersects(iconHitboxes.get(0))) {
            gp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        if (!mouseHitbox.intersects(iconHitboxes.get(1))) {
            gp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        if (!mouseHitbox.intersects(iconHitboxes.get(2))) {
            gp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        if (mouseHitbox.intersects(iconHitboxes.get(0)) && !iHaveSelected) {
            gp.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (mouseH.leftClick) {
                selectedRPS = com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.rock;
                sendReady = true;
                iHaveSelected = true;
            }
        }
        if (mouseHitbox.intersects(iconHitboxes.get(1)) && !iHaveSelected) {
            gp.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (mouseH.leftClick) {
                selectedRPS = com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.paper;
                sendReady = true;
                iHaveSelected = true;
            }
        }
        if (mouseHitbox.intersects(iconHitboxes.get(2)) && !iHaveSelected) {
            gp.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (mouseH.leftClick) {
                selectedRPS = com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.scissors;
                sendReady = true;
                iHaveSelected = true;
            }
        }
        if (mouseH.rightClick) {
            System.out.println(opponentSelection);
            selectedRPS = com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none;
            opponentSelection = com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rps.enums.selectedRPS.none;
            opponentHidden = true;
            opponentRecived = false;
            sendReady = false;
            iHaveSelected = false;
        }
    }
}