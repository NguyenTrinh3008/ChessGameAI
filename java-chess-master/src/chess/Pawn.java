package chess;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class Pawn extends Piece {
    
    private final int imageNumber = 0;
    
    public boolean enPassantOk = false;

    public Pawn(Point location, Color color) {
        this.numMoves = 0;
        this.color = color;
        this.location = location;
    }    

    private Pawn(Point location, Color color, int moves, boolean captureableEnPassant) {
        enPassantOk = captureableEnPassant;
        this.numMoves = moves;
        this.color = color;
        this.location = location;
    }

    public Piece clone() {
        return new Pawn(new Point(this.location.x, this.location.y),
                this.color, this.numMoves, this.enPassantOk);
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

    public List<Move> getValidMoves(Board board, boolean checkKing) {
        List<Move> moves = new ArrayList<Move>();

        // if no board given, return empty list
        if (board == null)
            return moves;

        // checks moves where the pawn advances a rank
        advance(board, moves);
        // checks moves where the pawn captures another piece
        capture(board, moves);
        // checks en passant moves
        enPassant(board, moves);

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

    private void advance(Board board, List<Move> moves) {
        int x = location.x;
        int y = location.y;
        
        Piece pc;
        Point pt;
        int move;
        
        if (color == Color.White)
            move = -1;
        else
            move = 1;
                
        pt = new Point(x, y + move);
        if (board.validLocation(pt)) {
            pc = board.getPieceAt(pt);            
            if(pc == null) {
                moves.add(new Move(this, pt, pc));     
                
                pt = new Point(x, y + move * 2);
                if (board.validLocation(pt)) {
                    pc = board.getPieceAt(pt);
                    if(pc == null && numMoves == 0)
                        moves.add(new Move(this, pt, pc));
                }
            } 
        }
    }

    private void capture(Board board, List<Move> moves) {
        int x = location.x;
        int y = location.y;
        
        Piece pc;
        Point pt;
        int move;
        
        if (color == Color.White)
            move = -1;
        else
            move = 1;
            
        pt = new Point(x - 1, y + move);
        if (board.validLocation(pt)) {
            pc = board.getPieceAt(pt);            
            if (pc != null)
                if(this.color != pc.getColor())
                    moves.add(new Move(this, pt, pc));    
        }
        pt = new Point(x + 1, y + move);
        if (board.validLocation(pt)) {
            pc = board.getPieceAt(pt);           
            if (pc != null)
                if(this.color != pc.getColor())
                    moves.add(new Move(this, pt, pc));       
        }
    }

    private void enPassant(Board board, List<Move> moves) {
        int x = location.x;
        int y = location.y; 
        
        if (this.color == Color.White && this.location.y == 3) {
            if(canCaptureEnPassant(board, new Point(x - 1, y)))
                moves.add(new Move(this, new Point(x - 1, y - 1),
                        board.getPieceAt(new Point(x - 1, y))));
            if(canCaptureEnPassant(board, new Point(x + 1, y)))
                moves.add(new Move(this, new Point(x + 1, y - 1),
                        board.getPieceAt(new Point(x + 1, y)))); 
        }
        if (this.color == Color.Black && this.location.y == 4) {
            if(canCaptureEnPassant(board, new Point(x - 1, y)))
                moves.add(new Move(this, new Point(x - 1, y + 1),
                        board.getPieceAt(new Point(x - 1, y))));
            if(canCaptureEnPassant(board, new Point(x + 1, y)))
                moves.add(new Move(this, new Point(x + 1, y + 1),
                        board.getPieceAt(new Point(x + 1, y))));            
        }
    }

    private boolean canCaptureEnPassant(Board board, Point pt) {
        Piece temp = board.getPieceAt(pt);
        if(temp != null)
            if (temp instanceof Pawn && temp.getColor() !=  this.color)
                if (((Pawn)temp).enPassantOk)
                    return true;
        return false;
    }
}
