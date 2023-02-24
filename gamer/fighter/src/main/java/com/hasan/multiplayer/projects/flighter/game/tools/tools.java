package com.hasan.multiplayer.projects.flighter.game.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    public static BufferedImage loadScaledSprite (Class<?> loaderClass, String path, int width, int height){
        try {
            BufferedImage loadImageNonScaled = ImageIO.read(loaderClass.getResourceAsStream(path));
            BufferedImage scaledCanvas = new BufferedImage (width, height, 2);
                scaledCanvas.createGraphics().drawImage(loadImageNonScaled, 0, 0, width, height, null, null);
            return scaledCanvas;
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
    public static List<BufferedImage> loadScaledSprite (Class<?> loaderClass, List<String> path, int width, int height){
        List<BufferedImage> list = new ArrayList<>();
        BufferedImage scaledCanvas;
        try {
            for (String address : path) {
                BufferedImage loadImageNonScaled = ImageIO.read(loaderClass.getResourceAsStream(address));
                scaledCanvas = new BufferedImage (width, height, 2);
                scaledCanvas.createGraphics().drawImage(loadImageNonScaled, 0, 0, width, height, null, null);
                list.add(scaledCanvas);
            }
            return list;
        } catch (Exception e) {
            System.err.println("[tools][loadScaledSprite][multi]: There was an error");
            System.err.println("[tools][loadScaledSprite][multi]: " + e.getMessage());
            System.err.println("[tools][loadScaledSprite][multi]: " + e.getCause());
            scaledCanvas = new BufferedImage (width, height, 2);
                scaledCanvas.createGraphics().fillRect(0, 0, width, height);
            list.add(scaledCanvas);
            return list;
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
