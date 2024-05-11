package chess;

import java.awt.Point;

public class CastleMove extends Move {  
    private Piece rook;
    private Point moveRookTo;

    public CastleMove(Piece king, Point moveKing, Piece rook, Point moveRook) {
        super(king, moveKing, null);
        this.moveRookTo = moveRook;
        this.rook = rook;
    }

    public Point getRookMoveTo() {
        return moveRookTo;
    }

    public Piece getRook() {
        return rook;
    }
}
