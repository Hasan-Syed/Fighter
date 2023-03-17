package com.hasan.multiplayer.projects.flighter.game.HUD;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notificationPosition;
import com.hasan.multiplayer.projects.flighter.game.HUD.notifyEnum.notifyType;
import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

/**
 * HUD
 * 
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class HUD {
    // GamePanel
    final gamePanel gp;
    // Notification Bar
    ScheduledExecutorService notification;
    int timeMultiplyer;
    // Selected Notification
    notifyBar currentlySelectedNotification;

    public HUD(gamePanel gp) {
        this.gp = gp;
        init();
    }

    /**
     * {@code init} Initialies defaults of the HUD
     */
    void init() {
        // Initialize Notification Bar
        gp.notificationList = new ArrayList<notifyBar>(); // Notification List
        this.notification = Executors.newScheduledThreadPool(1); // Initialize Notification Timer
        this.timeMultiplyer = 1000; // Seconds Multiplyer
    }

    /**
     * {@code addNewMidNotification} is used to add {@code notification} to the
     * display array, adds a notification to the middle of the screen.
     * 
     * @param ID       ID of the notification
     * @param message  Message to be Displayed
     * @param duration Display Duration Time
     * @param source   Message Display Source Class
     * @param type     Notification Type: success, error, warning
     */
    public void addNewMidNotification(int ID, String message, int duration, String source, notifyType type) {
        // Create a temporary notifyBar object
        notifyBar temp = new notifyBar(gp, ID, message, duration, source, type, notificationPosition.mid);
        // Add the temporary to the notification list
        gp.notificationList.add(temp);
    }

    /**
     * {@code} is used to check if a {@code notification} has already been deployed
     * to the display list, checks for Mid based {@code notifications}
     * 
     * @param ID     ID of the notification
     * @param source Message Display Source Class
     * @param type   Notification Type: success, error, warning
     * @return
     */
    public boolean alreadyAlertedMidNotification(int ID, String source, notifyType type) {
        boolean alertedCheckLST = gp.notificationList.stream()
                .filter((alertedCheck) -> alertedCheck.ID == ID)
                .filter((alertedCheck) -> alertedCheck.source == source)
                .filter((alertedCheck) -> alertedCheck.type == type)
                .filter((alertedCheck) -> alertedCheck.position == notificationPosition.mid)
                .collect(Collectors.toList()).isEmpty();
        if (!alertedCheckLST)
            return true;
        else
            return false;
    }

    /**
     * {@code} is used to check if a {@code notification} has already been deployed
     * to the display list, checks for Top based {@code notifications}
     * 
     * @param ID     ID of the notification
     * @param source Message Display Source Class
     * @param type   Notification Type: success, error, warning
     * @return
     */
    public boolean alreadyAlertedTopNotification(int ID, String source, notifyType type) {
        List<notifyBar> alertedCheckLST = gp.notificationList.stream()
                .filter((alertedCheck) -> alertedCheck.ID == ID)
                .filter((alertedCheck) -> alertedCheck.source == source)
                .filter((alertedCheck) -> alertedCheck.type == type)
                .filter((alertedCheck) -> alertedCheck.position == notificationPosition.top)
                .collect(Collectors.toList());
        if (alertedCheckLST.size() > 0)
            return true;
        else
            return false;
    }

    /**
     * {@code addNewTopNotification} is used to add {@code notification} to the
     * display array, adds a notification to the top of the screen.
     * 
     * @param ID       ID of the notification
     * @param message  Message to be Displayed
     * @param duration Display Duration Time
     * @param source   Message Display Source Class
     * @param type     Notification Type: success, error, warning
     */
    public void addNewTopNotification(int ID, String message, int duration, String source, notifyType type) {
        // Create a temporary notifyBar object
        notifyBar temp = new notifyBar(gp, ID, message, duration, source, type, notificationPosition.top);
        // Add the temporary to the notification list
        gp.notificationList.add(temp);
    }

    /**
     * {@code removeMidNotification} is used to remove {@code notification} from the
     * notification display Array, removes a notification from the middle of the
     * screen.
     * 
     * @param ID     ID of the Notification
     * @param source Message Display Source Class
     */
    public void removeMidNotification(int ID, String source) {
        List<notifyBar> removalContestents = gp.notificationList.stream()
                .filter((removalNotify) -> removalNotify.ID == ID)
                .filter((removalNotify) -> removalNotify.source == source)
                .filter((removalNotify) -> removalNotify.position == notificationPosition.mid)
                .collect(Collectors.toList());
        gp.notificationList.remove(removalContestents.get(0));
    }

    /**
     * {@code removeTopNotification}is used to remove {@code notification} from the
     * notification display Array, removes a notification from the top of th screen.
     * 
     * @param ID     ID of the Notification
     * @param source Message Display Source Class
     */
    public void removeTopNotification(int ID, String source) {
        List<notifyBar> removalContestents = gp.notificationList.stream()
                .filter((removalNotify) -> removalNotify.ID == ID)
                .filter((removalNotify) -> removalNotify.source == source)
                .filter((removalNotify) -> removalNotify.position == notificationPosition.top)
                .collect(Collectors.toList());
        gp.notificationList.remove(removalContestents.get(0));
    }

    /**
     * {@code notificationElementUpdate} is used to update/select/remove displayed
     * {@code notification}
     */
    private void notificationElementUpdate() {
        // Checks there is a notification in the queue, and a notification is currently
        // being displayed/
        if (!gp.displayingNotification && !gp.notificationList.isEmpty()) {
            // Get the First Notification in queue, and set it as a selected notification
            currentlySelectedNotification = gp.notificationList.get(0);
            // schedule the notification to Display, with the delay being 1 second
            notification.scheduleWithFixedDelay(currentlySelectedNotification, 0,
                    1, TimeUnit.SECONDS);
            // Get the notifications display position, and queue it for removal
            switch (currentlySelectedNotification.position) {
                // Position Mappng Top
                case top -> {
                    // Remove the notification from the notificaltion Queue
                    removeTopNotification(currentlySelectedNotification.ID, currentlySelectedNotification.source);
                }
                // Position Mappng Mid
                case mid -> {
                    // Remove the notification from the notificaltion Queue
                    removeMidNotification(currentlySelectedNotification.ID, currentlySelectedNotification.source);
                }
            }
        }
        // If currentlySlectedNotification isn't null (there is a notification selected)
        if (currentlySelectedNotification != null) {
            // Update the notification
            currentlySelectedNotification.update();
        }
    }

    /**
     * Updates all HUD Elements,
     */
    public void update() {
        notificationElementUpdate();
    }

    /**
     * used to draw Updates on to the screen
     *
     * @param g2d The super graphics Object
     */
    public void draw(Graphics2D g2d) {
        // assert currentlySelectedNotification != null;
        if (currentlySelectedNotification != null) {
            currentlySelectedNotification.draw(g2d);
        }
    }
}
