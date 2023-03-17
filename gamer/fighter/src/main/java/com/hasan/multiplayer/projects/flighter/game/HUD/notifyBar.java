package com.hasan.multiplayer.projects.flighter.game.HUD;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.RoundRectangle2D;
import java.util.TimerTask;
import java.awt.geom.Rectangle2D;

import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notificationPosition;
import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.KeyHandler;
import com.hasan.multiplayer.projects.flighter.game.tools.tools;

/**
 * The notifyBar is used to display a HUD notification on to the players screen. The notifyBar is controlled by its parent class HUD.
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class notifyBar extends TimerTask{
    // Notification Settings
    public int upTime;
    final int ID;
    final gamePanel gp;
    final KeyHandler keyH;
    final Dimension windowSize;
    final notifyType type;
    final notificationPosition position;
    final String message;
    final String source;
    boolean display;
    // box height and width
    int height, width;
    // Texture/Font Settings
    Image logo;
    Image texture;
    Font fontSettings;
    Color fontColor;
    Color backgroundLinning;
    RoundRectangle2D notifyBox;


    /**
     * notifybar() is to initialize the notification bar
     * @param gp
     * @param ID
     * @param message
     * @param type
     * @param position
     */
    public notifyBar(gamePanel gp, int ID, String message, int upTime, String source, notifyType type, notificationPosition position) {
        this.gp = gp;
        this.keyH = gp.keyH;
        this.ID = ID;
        this.windowSize = gp.windowSize;
        this.type = type;
        this.position = position;
        this.message = message;
        this.upTime = upTime;
        this.source = source;
        initializeDefaults();
    }

    private void initializeDefaults() {
        width = windowSize.width - 2;
        height = 60;
        switch (type) {
            case alert -> {
                fontSettings = new Font("arial", Font.BOLD, 20);
                backgroundLinning = Color.ORANGE;
                fontColor = Color.WHITE;
                logo = tools.loadScaledSprite(getClass(),
                        "/com/hasan/multiplayer/projects/flighter/resources/hudResources/notifyBar/alert.png", 32, 32);
            }
            case error -> {
                fontSettings = new Font("arial", Font.BOLD, 20);
                backgroundLinning = Color.RED;
                fontColor = Color.WHITE;
                logo = tools.loadScaledSprite(getClass(),
                        "/com/hasan/multiplayer/projects/flighter/resources/hudResources/notifyBar/warning.png", 32,
                        32);
            }
            case success -> {
                fontSettings = new Font("arial", Font.BOLD, 20);
                backgroundLinning = new Color(24, 69, 40);
                fontColor = Color.WHITE;
                logo = tools.loadScaledSprite(getClass(),
                        "/com/hasan/multiplayer/projects/flighter/resources/hudResources/notifyBar/success.png", 32,
                        32);
            }
            case newPlayer -> {
                fontSettings = new Font("arial", Font.BOLD, 20);
                backgroundLinning = Color.GREEN;
                fontColor = Color.WHITE;
                logo = tools.loadScaledSprite(getClass(),
                        "/com/hasan/multiplayer/projects/flighter/resources/hudResources/notifyBar/new-player-Spawn.png",
                        32, 32);
            }
            case info -> {
                fontSettings = new Font("arial", Font.BOLD, 20);
                backgroundLinning = new Color(129, 159, 224);
                fontColor = Color.WHITE;
                logo = tools.loadScaledSprite(getClass(),
                        "/com/hasan/multiplayer/projects/flighter/resources/hudResources/notifyBar/info.png",
                        32, 32);
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        }
    }

    public void removeMe() {
        upTime = 1;
    }

    public void update() {    
        if (!(upTime < 0))
            gp.displayingNotification = true;
        else
            gp.displayingNotification = false;
    
        if (gp.displayingNotification && keyH.escape) {
            removeMe();
        }
    }

    public void draw(Graphics2D g2d) {
        if (upTime > 0) {
            int fullWidthSize = 32 + 20;
            int fullHeightSize = 32 + 15;
            FontMetrics fontMaMetrics = g2d.getFontMetrics();
            // Set Font Settings
            g2d.setFont(fontSettings);
            // Get Text Bounds
            Rectangle2D textBounds = fontMaMetrics.getStringBounds(message, g2d);
            fullWidthSize += textBounds.getWidth();
            switch (position) {
                case mid -> {
                    Graphics2D mid = (Graphics2D) g2d.create((windowSize.width / 2) - width / 2,
                    (windowSize.height / 2) - height / 2, width, fullHeightSize);
                    int drawPointX = (width / 2 - fullWidthSize / 2) - fullWidthSize / 2;
                    // Draw Rectangle
                    mid.setColor(backgroundLinning);
                    mid.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    mid.fillRect(0, 0, width, fullHeightSize);

                    mid.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    mid.drawImage(logo, drawPointX, (fullHeightSize / 2) - (32 / 2), gp);
                    drawPointX += 32 + 10;
                    mid.drawString(message, drawPointX,
                            (fullHeightSize / 2) + (int) (textBounds.getHeight() / 2));

                    // Write up Source
                    {
                        mid.setFont(new Font("arial", Font.BOLD, 9));
                        mid.drawString(source, 0, fullHeightSize - 5);
                    }

                    mid.dispose();
                }
                case top -> {
                    Graphics2D top = (Graphics2D) g2d.create((windowSize.width / 2) - width / 2,
                    0, width, fullHeightSize);
                    int drawPointX = (width / 2 - fullWidthSize / 2) - fullWidthSize / 2;
                    // Draw Rectangle
                    top.setColor(backgroundLinning);
                    top.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    top.fillRect(0, 0, width, fullHeightSize);

                    top.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    top.drawImage(logo, drawPointX, (fullHeightSize / 2) - (32 / 2), gp);
                    drawPointX += 32 + 10;
                    top.drawString(message, drawPointX,
                            (fullHeightSize / 2) + (int) (textBounds.getHeight() / 2));

                    // Write up Source
                    {
                        top.setFont(new Font("arial", Font.BOLD, 9));
                        top.drawString(source, 0, fullHeightSize - 5);
                    }

                    top.dispose();
                }
                default -> {
                }
            }
        }
    }

    @Override
    public void run() {
        if (upTime == 0) {
            this.cancel();
            gp.notificationList.remove(this);
            gp.displayingNotification = false;
            return;
        }
        if (upTime >= 1){
            upTime -= 1;
        }
    }
}
