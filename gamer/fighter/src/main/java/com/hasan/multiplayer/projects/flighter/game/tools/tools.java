package com.hasan.multiplayer.projects.flighter.game.tools;

import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class tools {
    /**
     * LoadScaledSprite() is used to load a single Sprite
     * 
     * @param loaderClass
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Image loadScaledSprite (Class<?> loaderClass, String path, int width, int height){
        try {
            Image loadImageNonScaled = new ImageIcon(loaderClass.getResource(path)).getImage();
            return loadImageNonScaled.getScaledInstance(width, height, 0);
        } catch (Exception e) {
            System.err.println("[tools][loadScaledSprite][single]: There was an error");
            System.err.println("[tools][loadScaledSprite][single]: " + e.getMessage());
            System.err.println("[tools][loadScaledSprite][single]: " + e.getCause());
            BufferedImage scaledCanvas = new BufferedImage (width, height, 2);
                scaledCanvas.createGraphics().fillRect(0, 0, width, height);
            return scaledCanvas;
        }
    }
    /**
     * LoadScaledSprite() is used to load a single Sprite
     * 
     * @param loaderClass
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static List<Image> loadScaledSprite (Class<?> loaderClass, List<String> path, int width, int height){
        List<Image> list = new ArrayList<>();
        Image scaledCanvas;
        try {
            for (String address : path) {
                Image loadImageNonScaled = new ImageIcon(loaderClass.getResource(address)).getImage();
                list.add(loadImageNonScaled.getScaledInstance(width, height, 0));
            }
            return list;
        } catch (Exception e) {
            System.err.println("[tools][loadScaledSprite][multi]: There was an error");
            System.err.println("[tools][loadScaledSprite][multi]: " + e.getMessage());
            System.err.println("[tools][loadScaledSprite][multi]: " + e.getCause());
            scaledCanvas = new BufferedImage (width, height, 2);
            list.add(scaledCanvas);
            return list;
        }
    }

    /**
     * loadFont() is used to load a font
     * 
     * @param loaderClass The loaderClass
     * @param path Path to the Font
     * @return returns the Loaded Font
     */
    public static Font loadFont (Class<?> loaderClass, String path){
        Font newLoad;
        try {
            InputStream stream = loaderClass.getResourceAsStream(path);
            newLoad = Font.createFont(Font.TRUETYPE_FONT, stream);
            return newLoad;
        } catch (FontFormatException | IOException e) {
            System.err.println("[tools][loadFont]: There was an error, Defaulting to 'Arial'");
            System.err.println("[tools][loadFont]: " + e.getMessage());
            System.err.println("[tools][loadFont]: " + e.getCause());
            newLoad = new Font("Arial", 0, 25);
            return newLoad;
        }
    }

    public static int UUIDCreator () {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return number;
    }
}
