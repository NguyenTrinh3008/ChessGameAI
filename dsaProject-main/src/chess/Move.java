package chess;

import java.awt.Point;
import java.util.LinkedList;

public class Move {
    private Piece toMove;
    private Point moveTo;
    private Piece toCapture;
    private Piece InitialLocation;

//    private Linkedlist linkedlist;


    public Move(Piece toMove, Point moveTo, Piece toCapture) {
        this.toMove = toMove;
        this.moveTo = moveTo;
        this.toCapture = toCapture;
    }

    public Point getMoveTo() {
        return moveTo;
    }

    public Piece getPiece() {
        return toMove;
    }

    public Piece getCaptured() {
        return toCapture;
    }
}
