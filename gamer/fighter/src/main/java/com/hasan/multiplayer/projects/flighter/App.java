package com.hasan.multiplayer.projects.flighter;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import com.hasan.multiplayer.projects.flighter.game.gamePanel.gamePanel;

/**
 * Main Loader Class
 * @author Hasan Syed
 * @version 1.0
 * @since 1.0
 */
public class App extends JFrame
{
    public gamePanel gamePanel;

    public App () throws UnknownHostException, IOException{
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Fighter Game");
        setResizable(true);
        
        gamePanel = new gamePanel();
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
