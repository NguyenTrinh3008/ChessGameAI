/*
Name: Trinh Bình Nguyên
Purpose: Implements the ChristmasDecorator class, extending Decorator,
to apply a Christmas theme to a chess game.
*/


package Decor;

import Decor.Decorator;
import Decor.IComponent;
import chess.GamePanel;
import chess.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

public class ChristmasDecorator extends Decorator {
    public ChristmasDecorator(IComponent component) {
        super(component);
    }

    boolean imagesLoaded = false;

    private GamePanel gp;
    final Color lightColor = new Color(255, 253, 196);
    final Color darkColor = new Color(174, 241, 255);


    private void loadChristmasImages(){
        try {
            // initialize two arrays of bufferedImages
            BufferedImage[] whiteImages = new BufferedImage[6];
            BufferedImage[] blackImages = new BufferedImage[6];

            // if the PIECES folder doesn't exist, create it
            File directory = new File ("Christmas_src");
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    // the directory will be empty, so throw an exception
                    throw new Exception("The Christmas_src directory did not exist. " +
                            "It has been created. Ensure that it contains the files: \n");
                }
            }

            // load all white images
            whiteImages[0] = ImageIO.read(new File("Christmas_src/PWF.png"));
            whiteImages[1] = ImageIO.read(new File("Christmas_src/MWF.png"));
            whiteImages[2] = ImageIO.read(new File("Christmas_src/HWF.png"));
            whiteImages[3] = ImageIO.read(new File("Christmas_src/CWF.png"));
            whiteImages[4] = ImageIO.read(new File("Christmas_src/QWF.png"));
            whiteImages[5] = ImageIO.read(new File("Christmas_src/KWF.png"));

            // load all black images
            blackImages[0] = ImageIO.read(new File("Christmas_src/PBF.png"));
            blackImages[1] = ImageIO.read(new File("Christmas_src/MBF.png"));
            blackImages[2] = ImageIO.read(new File("Christmas_src/HBF.png"));
            blackImages[3] = ImageIO.read(new File("Christmas_src/CBF.png"));
            blackImages[4] = ImageIO.read(new File("Christmas_src/QBF.png"));
            blackImages[5] = ImageIO.read(new File("Christmas_src/KBF.png"));

            // set the white and black images in the Piece class
            Piece.setBlackImages(blackImages);
            Piece.setWhiteImages(whiteImages);

            // images loaded without errors
            imagesLoaded = true;
        } catch (Exception e) {
            //    status = GamePanel.GameStatus.Error;
            // create a message to inform the use about the error
            String message = "Could not load piece images. " +
                    "Check that all 12 images exist in the PIECES folder " +
                    "and are accessible to the program.\n" +
                    "The program will not function properly until this is resolved.\n\n" +
                    "Error details: " + e.getMessage();
            // display the message
            JOptionPane.showMessageDialog(gp, message, "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void addChristmasTheme(Graphics g, int sW, int sH) {
        // draw a light background
        g.setColor(lightColor);
        g.fillRect(0, 0, sW * 8, sH * 8);

        boolean dark = false;
        g.setColor(darkColor);
        // draw black squares
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (dark) {
                    g.fillRect(x * sW, y * sH, sW, sH);
                }
                dark = !dark;
            }
            dark = !dark;
        }
    }

    @Override
    public void drawBoard(Graphics g, int sW, int sH) {
        // TODO Auto-generated method stub
        super.drawBoard(g, sW, sH);
        addChristmasTheme(g, sW, sH);
        super.loadImage();
        loadChristmasImages();;
    }
}
