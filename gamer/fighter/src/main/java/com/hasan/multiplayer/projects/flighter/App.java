package com.hasan.multiplayer.projects.flighter;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.mouseHandler;
import com.hasan.multiplayer.projects.flighter.game.inputHandleing.mouseMotionHandler;

/**
 * Main Loader Class
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class App extends JFrame
{
    public gamePanel gamePanel;

    mouseMotionHandler mMotionH;
    mouseHandler mouseH;

    public App () throws UnknownHostException, IOException{
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Fighter Game");
        setResizable(true);

        mouseH = new mouseHandler();
        mMotionH = new mouseMotionHandler();

        addMouseListener(mouseH);
        addMouseMotionListener(mMotionH);
        
        gamePanel = new gamePanel(mMotionH, mouseH);
        add(gamePanel);
        pack();
        
        setLocationRelativeTo(null);
        setVisible(true);

        gamePanel.startGameThread();
    }

    public static void main( String[] args ) throws UnknownHostException, IOException{
        new App();
    }
}
