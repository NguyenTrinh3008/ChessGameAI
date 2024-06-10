package chess;

import LinkednStack.InCheckList;
import LinkednStack.PieceList;
import LinkednStack.TurnList;

import java.io.Serializable;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
public class Board implements Serializable, Cloneable {
    private Board previousState = null;
    private Piece.Color turn;
    private List<Piece> pieces = new ArrayList<Piece>();

    private Piece inCheck = null;
    private Piece Initial = null;
    private Piece lastMoved = null;
    private Ai ai = null;
    private Stack<Move> moveHistory = new Stack<>();
    private Stack<Piece> currentPiece = new Stack<>();
    private Stack <Point> moveTo = new Stack<>();
    private Stack<Piece.Color> nextTurn = new Stack<>();
    private Stack<Piece> InCheck = new Stack<>();
    private Stack<Board> currentState = new Stack<>();
    Point moveFrom;
    TurnList turnList = new TurnList();
    PieceList pieceList = new PieceList();
    InCheckList inCheckList = new InCheckList();
    Board changeBoard;


    public void setAi(Ai computerPlayer) {
        this.ai = computerPlayer;
    }

    public Ai getAi() {
        return ai;
    }

    public Piece getPieceInCheck() {
        return inCheck;
    }

    public Piece getLastMovedPiece() {
        return lastMoved;
    }

    public Point getInitialPieceLocation(Piece piece){
        return piece.getLocation();
    }

    public void setInitial(Piece InitialPiece){
        this.Initial = InitialPiece;
    }

    public Piece getInitialPiece(){
        return Initial;
    }

    public Board(boolean initPieces) {
        turn = Piece.Color.White;

        if (initPieces) {
            // black pieces
            pieces.add(new Pawn(new Point(0, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(1, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(2, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(3, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(4, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(5, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(6, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(7, 1), Piece.Color.Black));

            pieces.add(new Rook(new Point(0, 0), Piece.Color.Black));
            pieces.add(new Knight(new Point(1, 0), Piece.Color.Black));
            pieces.add(new Bishop(new Point(2, 0), Piece.Color.Black));
            pieces.add(new Queen(new Point(3, 0), Piece.Color.Black));
            pieces.add(new King(new Point(4, 0), Piece.Color.Black));
            pieces.add(new Bishop(new Point(5, 0), Piece.Color.Black));
            pieces.add(new Knight(new Point(6, 0), Piece.Color.Black));
            pieces.add(new Rook(new Point(7, 0), Piece.Color.Black));

            // white pieces
            pieces.add(new Pawn(new Point(0, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(1, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(2, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(3, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(4, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(5, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(6, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(7, 6), Piece.Color.White));

            pieces.add(new Rook(new Point(0, 7), Piece.Color.White));
            pieces.add(new Knight(new Point(1, 7), Piece.Color.White));
            pieces.add(new Bishop(new Point(2, 7), Piece.Color.White));
            pieces.add(new Queen(new Point(3, 7), Piece.Color.White));
            pieces.add(new King(new Point(4, 7), Piece.Color.White));
            pieces.add(new Bishop(new Point(5, 7), Piece.Color.White));
            pieces.add(new Knight(new Point(6, 7), Piece.Color.White));
            pieces.add(new Rook(new Point(7, 7), Piece.Color.White));
        }
    }

    private Board(Piece.Color turn, Board previousState, List<Piece> pieces,
                  Piece lastMoved, Piece inCheck, Ai ai) {
        this.turn = turn;
        if (inCheck != null)
            this.inCheck = inCheck.clone();
        if (lastMoved != null)
            this.lastMoved = lastMoved.clone();
        this.ai = ai;
        this.previousState = previousState;
        for(Piece p : pieces) {
            this.pieces.add(p.clone());
        }
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public Piece getPieceAt(Point p) {
        for(Piece pc : pieces) {
            if(pc.getLocation().x == p.x &&
                    pc.getLocation().y == p.y)
                return pc;
        }
        return null;
    }

    public void removePiece(Piece p) {
        if (pieces.contains(p)) {
            pieces.remove(p);
            return;
        }
    }

    public void addPiece(Piece p) {
        pieces.add(p);
    }

    public void removePieceAt(Point p) {
        Piece temp = null;
        for(Piece pc : pieces) {
            if (pc.getLocation().equals(p)) {
                temp = pc;
                break;
            }
        }
        if (temp != null)
            pieces.remove(temp);
    }

    public Piece.Color getTurn() {
        return turn;
    }

    public void doMove(Move m, boolean playerMove) {
        this.previousState = this.clone();
        // implementing en passant rule
        for(Piece pc : pieces)
            if (pc.getColor() == turn && pc instanceof Pawn)
                ((Pawn)pc).enPassantOk = false;

        // if move is castling
        if (m instanceof CastleMove) {
            CastleMove c = (CastleMove)m;
            c.getPiece().moveTo(c.getMoveTo());
            c.getRook().moveTo(c.getRookMoveTo());
        } else {
            if(m.getCaptured() != null);
            this.removePiece(m.getCaptured());

            // implementing en passant rule
            if (m.getPiece() instanceof Pawn)
                if (Math.abs(m.getPiece().getLocation().y - m.getMoveTo().y) == 2)
                    ((Pawn)m.getPiece()).enPassantOk = true;

            m.getPiece().moveTo(m.getMoveTo());

            // promote pawn if reached final rank
            checkPawnPromotion(m.getPiece(), playerMove);
        }
        turnList.InsertFirst(turn);
        pieceList.InsertFirst(m.getPiece());
        inCheckList.InsertFirst(kingInCheck());
        moveHistory.push(m);
        this.lastMoved = pieceList.getPiece();
        this.inCheck = inCheckList.getInCheck();



        // change the color of pieces moving next
        turn = Piece.Color.values()[(turn.ordinal() + 1) % 2];
    }

    private void checkPawnPromotion(Piece pawn, boolean showDialog) {
        if(pawn instanceof Pawn && (pawn.getLocation().y == 0 || pawn.getLocation().y == 7)) {
            Piece promoted;

            // if ai, promote automatically to queen
            if (!showDialog || (ai != null && ai.getColor() == pawn.getColor())) {
                promoted = new Queen(pawn.getLocation(), pawn.getColor());
            } else {
                // else, give the player a choice
                Object type = javax.swing.JOptionPane.showInputDialog(
                        null, "",
                        "Choose promotion:",
                        javax.swing.JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[] { "Queen", "Rook", "Bishop", "Knight" },
                        "Queen");

                // will be null if JOptionPane is cancelled or closed
                // default to queen in that case
                if (type == null)
                    type = "Queen";

                // interpret the JOptionPane result
                if (type.toString().equals("Queen"))
                    promoted = new Queen(pawn.getLocation(), pawn.getColor());
                else if (type.toString().equals("Rook"))
                    promoted = new Rook(pawn.getLocation(), pawn.getColor());
                else if (type.toString().equals("Bishop"))
                    promoted = new Bishop(pawn.getLocation(), pawn.getColor());
                else
                    promoted = new Knight(pawn.getLocation(), pawn.getColor());
            }

            // remove pawn and add promoted piece to board
            pieces.remove(pawn);
            pieces.add(promoted);
        }
    }


    public Board tryMove(Move m) {
        // creates a copy of the board
        Board helper = this.clone();

        if (m instanceof CastleMove) {
            // creates a copy of the move for the copied board
            CastleMove c = (CastleMove)m;
            Piece king = helper.getPieceAt(c.getPiece().getLocation());
            Piece rook = helper.getPieceAt(c.getRook().getLocation());

            // performs the move on the copied board
            helper.doMove(new CastleMove(king, c.getMoveTo(),
                    rook, c.getRookMoveTo()), false);
        } else {
            // creates a copy of the move for the copied board
            Piece capture = null;
            if(m.getCaptured() != null)
                capture = helper.getPieceAt(m.getCaptured().getLocation());

            Piece moving = helper.getPieceAt(m.getPiece().getLocation());

            // performs the move on the copied board
            helper.doMove(new Move(moving,
                    m.getMoveTo(), capture), false);
        }

        // returns the copied board with the move executed
        return helper;
    }

    /**
     * Used to find out if a king is in check
     * @return a Piece (King) if one is in check, else null
     */
    private Piece kingInCheck() {
        // go through all the pieces on the board
        for(Piece pc : pieces)
            // go through all the moves that can be made by the piece
            for(Move mv : pc.getValidMoves(this, false))
                // if a move would result in a king being captured
                if (mv.getCaptured() instanceof King) {
                    // that king is in check
                    this.inCheck = mv.getCaptured();
                    // return it.
                    return mv.getCaptured();
                }
        return null;
    }

    public boolean movePutsKingInCheck(Move m, Piece.Color kingColor) {
        // create a copy of the board
        Board helper = tryMove(m);

        // go through all the pieces on the board
        for(Piece pc : helper.getPieces())
            // if the color is different than the color
            // of the king we are checking for
            if (pc.color != kingColor)
                // go through all of it's available moves
                for(Move mv : pc.getValidMoves(helper, false))
                    // if a move would result in the capture of a king
                    if (mv.getCaptured() instanceof King)
                        return true;
        return false;
    }

    public boolean gameOver() {
        // create an array for all the moves that can be made by
        // black pieces, white pieces
        List<Move> whiteMoves = new ArrayList<Move>();
        List<Move> blackMoves = new ArrayList<Move>();

        // all moves to the arrays for all pieces
        for(Piece p : pieces) {
            if(p.getColor() == Piece.Color.White)
                whiteMoves.addAll(p.getValidMoves(this, true));
            else
                blackMoves.addAll(p.getValidMoves(this, true));
        }

        // if either side can make no valid moves, the game is over
        return (whiteMoves.size() == 0 || blackMoves.size() == 0);
    }

    @Override
    public Board clone() {
        return new Board(turn, previousState, pieces, lastMoved, inCheck, ai);
    }

    public Board getPreviousState() {
        if(previousState != null)
            return previousState;
        return this;
    }

    public boolean validLocation(Point p) {
        return (p.x >= 0 && p.x <= 7) && (p.y >= 0 && p.y <= 7);
    }


    public void getPrevious(GamePanel gp){
        if(pieceList.isEmpty()){
            // this.previousState = changeBoard.getPreviousState();
            return;
        }
        currentState.push(this.clone());
        Board previousBoard = this.getPreviousState();

        nextTurn.push(turnList.getTurn());
        currentPiece.push(pieceList.getPiece());
        InCheck.push(inCheckList.getInCheck());
        this.turn = turnList.getTurn();
        turnList.RemoveFirst();
        this.inCheck = inCheckList.getInCheck();
        inCheckList.RemoveFirst();
        this.lastMoved = pieceList.getPiece();
        pieceList.RemoveFirst();
        this.pieces = new ArrayList<>(previousBoard.getPieces());;
        moveTo.push(lastMoved.getLocation());
        lastMoved = null;
        gp.getBoard().setPreviousState(previousBoard);
        gp.repaint();

    }

    public void setChangeBoard(Board changeBoard) {
        this.changeBoard = changeBoard;
    }

    public Board getChangeBoard() {
        return changeBoard;
    }



    public void getRedo(GamePanel gp) {
        if (currentPiece.isEmpty() || nextTurn.isEmpty() || InCheck.isEmpty()) {
            return;
        }

        Board originalState = this.clone();
        System.out.println("Before Redo - " + currentPiece.size() + ", " + nextTurn.size() + ", " + InCheck.size());
        Board currentBoard = currentState.pop();
        Piece resetPiece = currentPiece.pop();
        pieceList.InsertFirst(resetPiece);
        turnList.InsertFirst(nextTurn.pop());
        inCheckList.InsertFirst(InCheck.pop());
        this.turn = turnList.getTurn();
        this.inCheck = inCheckList.getInCheck();
        this.lastMoved = pieceList.getPiece();
        this.pieces = new ArrayList<>(currentBoard.getPieces());
        System.out.println(resetPiece.getLocation());
        System.out.println("After Redo - " + currentPiece.size() + ", " + nextTurn.size() + ", " + InCheck.size());
        changeBoard = this;
        this.setState(changeBoard);
        gp.getBoard().setState(currentBoard);
        gp.repaint();
        this.previousState = originalState;
    }

    public void setPreviousState(Board board){
        this.previousState = board.getPreviousState();
    }

    public void setState(Board board){
        this.previousState = board.clone();
    }

    public Board getState(){
        return this.previousState;
    }

    public  void setTurn(Piece.Color turn){
        this.turn = turn;
    }


}
