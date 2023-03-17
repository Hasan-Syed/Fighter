package com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.objectType.objects.rpsMiniGame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.hasan.multiplayer.projects.flighter.game.gameObjects.gameObject;
import com.hasan.multiplayer.projects.flighter.game.gameObjects.entities.playerType.superPlayer;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.KeyHandler;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.mouseMotionHandler;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

/**
 * A mini game
 * 
 * @author Hasan Syed
 * @since 1.0
 * @version 1.0
 */
public class rps extends gameObject {
    // Fonts
    Font playerInfoFont;
    // Game Settings
    private final Dimension windowSize;
    private Image challengerInitSprite;
    private int gameLevel;
    private final int width, height;
    // Assets
    private Image rock;
    private Rectangle2D rockHitBox = new Rectangle2D.Double();
    private Image paper;
    private Rectangle2D paperHitBox = new Rectangle2D.Double();
    private Image scissors;
    private Rectangle2D scissorsHitBox = new Rectangle2D.Double();
    // Connected Player
    public List<superPlayer> engagedPlayers;
    // Is the game Active?
    public boolean active = false;
    // Max Players that can connect to the game
    private final int maxPlayers;

    public JSONObject getSpawn() {
        JSONObject spawn = new JSONObject()
                .put("name", name)
                .put("ID", ID)
                .put("type", "game")
                .put("location", new JSONObject()
                        .put("x", worldPosition.getX())
                        .put("y", worldPosition.getY()));
        return spawn;
    }

    public void setSpawn(JSONObject spawn) {
        ID = spawn.getInt("ID");
        name = spawn.getString("name");
        this.worldPosition.x = spawn.getJSONObject("location").getInt("x");
        this.worldPosition.y = spawn.getJSONObject("location").getInt("y");
    }

    public rps(gamePanel gp) {
        super(gp, com.hasan.multiplayer.projects.flighter.game.gameObjects.enums.gameObjectType.minigame);
        this.name = "rps";
        this.ID = tools.UUIDCreator();
        windowSize = gp.windowSize;
        maxPlayers = 2;
        worldPosition = new Point(45, 45);
        hitbox = new Rectangle();
        width = 150;
        height = 100;
        loadAssets();
        engagedPlayers.add(gp.player);
    }

    private void loadAssets() {
        engagedPlayers = new ArrayList<>(2);
        // engagedPlayers.add(this.gp.player);
        // Game Sprite
        challengerInitSprite = tools.loadScaledSprite(getClass(),
                "/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/challenger.gif", width, height);
        // Game Assets
        rock = tools.loadScaledSprite(getClass(),
                "/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/assets/rock.png", 48, 48);
        paper = tools.loadScaledSprite(getClass(),
                "/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/assets/paper.png", 48, 48);
        scissors = tools.loadScaledSprite(getClass(),
                "/com/hasan/multiplayer/projects/flighter/resources/gameResources/rps/assets/scissors.png", 48, 48);
        // Fonts
        playerInfoFont = tools.loadFont(getClass(),
                "/com/hasan/multiplayer/projects/flighter/resources/gameResources/font/Righteous-Regular.ttf");
    }
    private KeyHandler keyH = gp.keyH;
    private mouseMotionHandler mMotionH = gp.mMotionH;
    private void inputHandleing() {
        // Check If the Screem is active, and Player wants to leave
        if (keyH.escape) {
            active = false;
            gp.player.engaged = false;
            engagedPlayers.remove(gp.player);
            keyH.escape = false;
        }
        if (active) {
            Point mousePosition;
            mousePosition = mMotionH.mouseLoc;
            // Motion Handler
            {

                Rectangle mousePos = new Rectangle(mousePosition, new Dimension(1, 1));

                if (mousePos.intersects(rockHitBox)) {
                    gp.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    gp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
            // Mouse
            {

            }
        }
    }

    @Override
    public void update() {
        if (active) {
            inputHandleing();
        }
        hitbox.setBounds(worldPosition.x, worldPosition.y, width, height);

        for (superPlayer player : gp.players) {
            if (hitbox.intersects(player.hitbox) && engagedPlayers.size() < 2 && !player.engaged) {
                player.engaged = true;
                engagedPlayers.add(player);
            }
        }
        if (hitbox.intersects(gp.player.hitbox) && engagedPlayers.size() < 2 && !gp.player.engaged) {
            gp.player.engaged = true;
            active = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!active) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            g2d.draw(hitbox);
            g2d.drawImage(challengerInitSprite, worldPosition.x, worldPosition.y, gp);
        }
        if (active) {
            int offset = 10;
            int x = offset, y = offset, wx = windowSize.width - offset, hy = windowSize.height - offset;
            Graphics2D g2d = (Graphics2D) g.create(x, y, wx, hy);
            g2d.setColor(new Color(79, 79, 79));
            g2d.fill3DRect(0, 0, wx - offset, hy - offset, true);
            drawGameContent(g2d);
        }
    }

    private void drawGameContent(Graphics2D g2d) {
        Graphics2D player1, player2;
        // Draw Round Text
        {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2d.drawString("Round: " + gameLevel, 5, 20);
            g2d.drawLine(0, 21, 100, 21);
        }
        {
            if (engagedPlayers.size() == 2) {
                player1 = (Graphics2D) g2d.create(0, 50, (int) (windowSize.getWidth() / 2) - 20,
                        (int) (windowSize.getHeight() - 80));
                RoundRectangle2D playerFrst = new RoundRectangle2D.Double(0, 50, (windowSize.getWidth() / 2) - 20,
                        (windowSize.getHeight() - 80), 2.5, 2.5);
                g2d.draw(playerFrst);
                drawPlayerBoxes(player1, 0);
                player2 = (Graphics2D) g2d.create((int) (windowSize.getWidth() / 2) - 1, 50,
                        (int) (windowSize.getWidth() / 2) - 20, (int) (windowSize.getHeight() - 80));
                RoundRectangle2D playerScnd = new RoundRectangle2D.Double((windowSize.getWidth() / 2) - 1, 50,
                        (windowSize.getWidth() / 2) - 20, (windowSize.getHeight() - 80), 2.5, 2.5);
                g2d.draw(playerScnd);
                drawPlayerBoxes(player2, 1);
            } else {
                g2d.drawString("Waiting for a second player...", 50, 50);
            }
        }
        g2d.drawRect(mMotionH.mouseLoc.x, mMotionH.mouseLoc.y, 1, 1);
    }

    private void drawPlayerBoxes(Graphics2D playerBox, int id) {
        superPlayer player = engagedPlayers.get(id);
        playerBox.setFont(playerInfoFont);
        int boxHeight = (int) windowSize.getHeight() - 80;
        playerBox.setFont(playerBox.getFont().deriveFont(Font.PLAIN, 48F));
        playerBox.drawString(player.name, 5, 48);
        playerBox.setFont(playerBox.getFont().deriveFont(Font.PLAIN, 15F));
        playerBox.drawString(player.ID + "", 5, 50);

        // Bottom of the Recs
        {
            int fullLength;
            int placedSpace = 10;
            int itemWidth = 48;
            int itemHeight = 48;
            {
                int bufferArea = 60;
                int placedItemsWidth = itemWidth * 3;
                fullLength = placedItemsWidth + placedSpace + bufferArea;
            }
            int startPosition;
            {
                startPosition = ((380 / 2) + 20) - (fullLength / 2);
            }
            // Place RPS Buttons
            {
                int buttonHeight = boxHeight - 48 - 10;
                int currentPosX = startPosition;
                // Rock
                {
                    playerBox.drawImage(rock, currentPosX, buttonHeight, gp);
                    if (!player.remoteControlled) {
                        rockHitBox.setFrame(currentPosX, (buttonHeight + itemHeight + 10), itemWidth, itemHeight);
                    }
                }
                // Paper
                {
                    currentPosX += (itemWidth + placedSpace);
                    playerBox.drawImage(paper, currentPosX, buttonHeight, gp);
                    if (!player.remoteControlled) {
                        paperHitBox.setFrame(currentPosX, (buttonHeight + itemHeight + 10), itemWidth, itemHeight);
                    }
                }
                // Scissors
                {
                    currentPosX += (itemWidth + placedSpace);
                    playerBox.drawImage(scissors, currentPosX, buttonHeight, gp);
                    if (!player.remoteControlled) {
                        scissorsHitBox.setFrame(currentPosX, (buttonHeight + itemHeight + 10), itemWidth, itemHeight);
                    }
                }
            }
        }

        playerBox.dispose();
    }

    @Override
    public void drawDebugMenu(Graphics2D g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'drawDebugMenu'");
    }

    @Override
    public JSONObject getUpdate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUpdate'");
    }

    @Override
    public void setUpdate(JSONObject Update) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpdate'");
    }

}
