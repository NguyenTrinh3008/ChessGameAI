/*
Name: Trinh Bình Nguyên
Purpose: Defines the Knight class, a subclass of Piece, representing a knight chess piece.
*/
package chess;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
public class Knight extends Piece{
       
    private final int imageNumber = 1;

    public Knight(Point location, Color color) {
        numMoves = 0;
        this.color = color;
        this.location = location;
    }

    private Knight(Point location, Color color, int moves) {
        this.numMoves = moves;
        this.color = color;
        this.location = location;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public BufferedImage getWhiteImage() {
        return whiteImages[imageNumber];
    }

    public BufferedImage getBlackImage() {
        return blackImages[imageNumber];
    }

    public Piece clone() {
        return new Knight(new Point(this.location.x, this.location.y),
                this.color, this.numMoves);
    }

    public List<Move> getValidMoves(Board board, boolean checkKing) {       
        int x = location.x;
        int y = location.y;

        List<Move> moves = new ArrayList<Move>();

        // if no board given, return empty list
        if (board == null)
            return moves;
        
        // check L-shapes
        addIfValid(board, moves, new Point(x + 1, y + 2));
        addIfValid(board, moves, new Point(x - 1, y + 2));
        addIfValid(board, moves, new Point(x + 1, y - 2));
        addIfValid(board, moves, new Point(x - 1, y - 2));
        addIfValid(board, moves, new Point(x + 2, y - 1));
        addIfValid(board, moves, new Point(x + 2, y + 1));
        addIfValid(board, moves, new Point(x - 2, y - 1));
        addIfValid(board, moves, new Point(x - 2, y + 1));    

        // check that move doesn't put own king in check
        if (checkKing)
            for(int i = 0; i < moves.size(); i++)
                if (board.movePutsKingInCheck(moves.get(i), this.color)) {
                    // if move would put king it check, it is invalid and
                    // is removed from the list
                    moves.remove(moves.get(i));
                    // iterator is decremented due to the size of the list
                    // decreasing.
                    i--;
                }
        return moves;
    }

    private void addIfValid(Board board, List<Move> list, Point pt) {
        // if the location is valid
        if(board.validLocation(pt)) {
            // and the location does not contain same color piece
            Piece pc = board.getPieceAt(pt);
            if(pc == null || pc.getColor() != this.color) {
                // all the move to the list
                list.add(new Move(this, pt, pc));
            }
        }
    }
}
