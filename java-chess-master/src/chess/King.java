package chess;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class King extends Piece{
       
    private final int imageNumber = 5;

    public King(Point location, Color color) {
        numMoves = 0;
        this.color = color;
        this.location = location;
    }

    private King(Point location, Color color, int moves) {
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
        return new King(new Point(this.location.x, this.location.y),
                this.color, this.numMoves);
    }

    public List<Move> getValidMoves(Board board, boolean checkKing) {
        int x = location.x;
        int y = location.y;

        List<Move> moves = new ArrayList<Move>();

        // if no board given, return empty list
        if (board == null)
            return moves;

        // add moves around the king if they are valid
        addIfValid(board, moves, new Point(x - 1, y - 1));
        addIfValid(board, moves, new Point(x, y - 1));
        addIfValid(board, moves, new Point(x + 1, y - 1));
        addIfValid(board, moves, new Point(x + 1, y));
        addIfValid(board, moves, new Point(x + 1, y + 1));
        addIfValid(board, moves, new Point(x, y + 1));
        addIfValid(board, moves, new Point(x - 1, y + 1));
        addIfValid(board, moves, new Point(x - 1, y));

        // castling
        if (this.numMoves == 0) {
            if (checkKing && this != board.getPieceInCheck())
            {
                List<Piece> pieces = board.getPieces();
                List<Piece> okRooks = new ArrayList<Piece>();

                // finds rooks available for castling
                for(int i = 0; i < pieces.size(); i++)
                    if (pieces.get(i).getColor() == this.color &&
                        pieces.get(i) instanceof Rook &&
                        pieces.get(i).getNumberOfMoves() == 0)
                        okRooks.add(pieces.get(i));

                // for each eligible rook
                for(Piece p : okRooks) {
                    boolean canCastle = true;
                    // if on right side of board
                    if (p.getLocation().x == 7) {
                        // if there are pieces between the king and the rook
                        for(int ix = this.location.x + 1; ix < 7; ix++) {
                            if (board.getPieceAt(new Point(ix, y)) != null) {
                                // castling is not possible
                                canCastle = false;
                                break;
                            }
                        }
                        if (canCastle)
                            moves.add(new CastleMove(this, new Point(x + 2, y),
                                    p, new Point(x + 1, y)));
                    // if on left side of board
                    } else if (p.getLocation().x == 0) {
                        // if there are pieces between the king and the rook
                        for(int ix = this.location.x - 1; ix > 0; ix--) {
                            if (board.getPieceAt(new Point(ix, y)) != null) {
                                // castling is not possible
                                canCastle = false;
                                break;
                            }
                        }
                        if (canCastle)
                            moves.add(new CastleMove(this, new Point(x - 2, y),
                                    p, new Point(x - 1, y)));                    
                    }
                }
            }
        }

        // check move doesn't self in check
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
