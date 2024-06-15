/*
Name: Trinh Bình Nguyên
Purpose: Defines the abstract Piece class, representing a chess piece with common properties and behaviors.
*/

package chess;

import java.awt.Point;
import java.util.List;
import java.io.Serializable;
import java.awt.image.BufferedImage;

public abstract class Piece implements Serializable, Cloneable{
    public static enum Color {White, Black};

    // [0]:pawn [1]:knight [2]:bishop [3]:rook [4]:queen [5]:king
    protected static BufferedImage[] whiteImages;
    protected static BufferedImage[] blackImages;
    
    protected int numMoves;
    protected Color color;
    protected Point location;

    public int getNumberOfMoves() {
        return numMoves;
    }

    public Color getColor() {
        return this.color;
    }

    public void moveTo(Point p) {
        this.location = p;
        numMoves++;
    }

    public Point getLocation() {
        return this.location;
    }

    public abstract int getImageNumber() ;

    public abstract BufferedImage getWhiteImage() ; 

    public abstract BufferedImage getBlackImage() ;
    public abstract List<Move> getValidMoves(Board board, boolean checkKing);
    @Override
    public abstract Piece clone();
    public static void setWhiteImages(BufferedImage[] images) {
        whiteImages = images;
    }

    public static void setBlackImages(BufferedImage[] images) {
        blackImages = images;
    }

}
