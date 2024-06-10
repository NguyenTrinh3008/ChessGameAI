package Decor;

import chess.GamePanel;
import chess.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

public class NewyearDecorator extends Decorator {

    boolean imagesLoaded = false;

    private GamePanel gp;

    public NewyearDecorator(IComponent component) {
        super(component);
        // TODO Auto-generated constructor stub
    }

    final Color lightColor = new Color(255, 253, 196);
    final Color darkColor = new Color(174, 241, 255);

    private void addNewYearTheme(Graphics g, int sW, int sH) {
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

    private void loadNewYearImages(){
        try {
            // initialize two arrays of bufferedImages
            BufferedImage[] whiteImages = new BufferedImage[6];
            BufferedImage[] blackImages = new BufferedImage[6];

            // if the PIECES folder doesn't exist, create it
            File directory = new File ("NewYear_src");
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    // the directory will be empty, so throw an exception
                    throw new Exception("The PIECES directory did not exist. " +
                            "It has been created. Ensure that it contains the files: \n" );
                }
            }


            // load all white images
            whiteImages[0] = ImageIO.read(new File("NewYear_src/chess-pawn-white.png"));
            whiteImages[1] = ImageIO.read(new File("NewYear_src/chess-knight-white.png"));
            whiteImages[2] = ImageIO.read(new File("NewYear_src/chess-bishop-white.png"));
            whiteImages[3] = ImageIO.read(new File("NewYear_src/chess-rook-white.png"));
            whiteImages[4] = ImageIO.read(new File("NewYear_src/chess-queen-white.png"));
            whiteImages[5] = ImageIO.read(new File("NewYear_src/chess-king-white.png"));

            // load all black images
            blackImages[0] = ImageIO.read(new File("NewYear_src/chess-pawn-black.png"));
            blackImages[1] = ImageIO.read(new File("NewYear_src/chess-knight-black.png"));
            blackImages[2] = ImageIO.read(new File("NewYear_src/chess-bishop-black.png"));
            blackImages[3] = ImageIO.read(new File("NewYear_src/chess-rook-black.png"));
            blackImages[4] = ImageIO.read(new File("NewYear_src/chess-queen-black.png"));
            blackImages[5] = ImageIO.read(new File("NewYear_src/chess-king-black.png"));
            // set the white and black images in the Piece class
            Piece.setBlackImages(blackImages);
            Piece.setWhiteImages(whiteImages);

            // images loaded without errors
            imagesLoaded = true;
        } catch (Exception e) {
            // status = GamePanel.GameStatus.Error;
            // create a message to inform the use about the error
            String message = "Could not load piece images. " +
                    "Check that all 12 images exist in the folder " +
                    "and are accessible to the program.\n" +
                    "The program will not function properly until this is resolved.\n\n" +
                    "Error details: " + e.getMessage();
            // display the message
            JOptionPane.showMessageDialog(gp, message, "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void drawBoard(Graphics g, int sW, int sH) {
        // TODO Auto-generated method stub

        super.drawBoard(g, sW, sH);
        addNewYearTheme(g, sW, sH);
        super.loadImage();
        loadNewYearImages();
    }

}
