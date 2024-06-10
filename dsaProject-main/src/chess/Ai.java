package chess;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
public class Ai implements Serializable{
    private Piece.Color aiColor;
    private int depth;
    public Ai(Piece.Color color, int depth) {
        this.aiColor = color;
        this.depth = depth;
    }

    public Piece.Color getColor() {
        return aiColor;
    }

    public Move getMove(Board game) {
        // if given a null board, return null
        if (game == null)
            return null;
        // if it isn't the ai's turn, return null
        if (game.getTurn() != aiColor)
            return null;

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // initialize best value and best move variables
        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;

        // get the start time
        long startTime = System.currentTimeMillis();

        // get the best move for the AI (max) from the available moves
        for (int currentDepth = 1; currentDepth <= depth; currentDepth++) {
            final int finalDepth = currentDepth;
            List<Future<MoveValue>> futures = new ArrayList<>();
            for (Move m : getMoves(game)) {
                // Create a new final variable and assign 'm' to it
                final Move finalMove = m;

                // submit tasks to executor service
                futures.add(executor.submit(() -> {
                    // Use 'finalMove' and 'finalDepth' instead of 'm' and 'currentDepth' inside the lambda
                    int moveValue = min(game.tryMove(finalMove), finalDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    return new MoveValue(finalMove, moveValue);
                }));
            }

            // find the best move
            for (Future<MoveValue> future : futures) {
                try {
                    MoveValue moveValue = future.get();
                    if (moveValue.value > bestValue || bestValue == Integer.MIN_VALUE) {
                        bestValue = moveValue.value;
                        bestMove = moveValue.move;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                }

                // check if we're running out of time
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime > 15000) {
                    executor.shutdownNow();
                    return bestMove;
                }
            }
        }

        if (!executor.isShutdown()) {
            executor.shutdown();
        }

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        return bestMove;
    }
    private static class MoveValue {
        public final Move move;
        public final int value;

        public MoveValue(Move move, int value) {
            this.move = move;
            this.value = value;
        }
    }
    private int max(Board game, int depth, int alpha, int beta) {
        // end search if game over or depth limit reached
        if (depth == 0)
            return valueOfBoard(game);

        List<Move> possibleMoves = getMoves(game);

        // if no moves can be made, game has ended
        if (possibleMoves.size() == 0)
            return valueOfBoard(game);

        // get the best move for the ai (max) from the available moves
        for(Move m : possibleMoves) {
            // get the value of the move
            int moveValue = min(game.tryMove(m), depth - 1, alpha, beta);

            // see if it is better than previous best move
            if (moveValue > alpha) {
                alpha = moveValue;
            }
            if (alpha >= beta)
                return alpha;
        }

        return alpha;
    }

    private int min(Board game, int depth, int alpha, int beta) {
        // end search if game over or depth limit reached
        if (depth == 0)
            return valueOfBoard(game);

        List<Move> possibleMoves = getMoves(game);

        // if no moves can be made, game has ended
        if (possibleMoves.size() == 0)
            return valueOfBoard(game);

        // get the best move for the player (min) from the available moves
        for(Move m : possibleMoves) {
            int moveValue = max(game.tryMove(m), depth - 1, alpha, beta);
            if (moveValue < beta) {
                beta = moveValue;
            }

            if (alpha >= beta)
                return beta;
        }
        return beta;
    }

    private List<Move> getMoves(Board game) {
        // initialize an arraylist
        List<Move> moves = new ArrayList<Move>();

        // for each piece
        for (Piece p : game.getPieces())
            // of the color that moves next
            if (p.getColor() == game.getTurn())
                // add all valid moves to the list
                moves.addAll(p.getValidMoves(game, true));

        // sort the moves based on some heuristic
        moves.sort((m1, m2) -> {
            int v1 = m1.getCaptured() != null ? valueOfPiece(m1.getCaptured()) : 0;
            int v2 = m2.getCaptured() != null ? valueOfPiece(m2.getCaptured()) : 0;
            return Integer.compare(v2, v1);  // sort in descending order
        });

        return moves;
    }
    private int valueOfBoard(Board gameBoard) {
        int value = 0;
        int aiPieces = 0;
        int aiMoves = 0;
        int playerPieces = 0;
        int playerMoves = 0;
        int aiCaptures = 0;
        int playerCaptures = 0;
        for(Piece pc : gameBoard.getPieces())
            if(pc.getColor() == aiColor) {
                // account for number of pieces on board
                aiPieces += valueOfPiece(pc);

                if (aiColor == gameBoard.getTurn())
                {
                    List<Move> validMoves = pc.getValidMoves(gameBoard, true);
                    for(Move m : validMoves) {
                        // account for how many moves can be made
                        aiMoves++;
                        if (m.getCaptured() != null) {
                            // account for possible captures
                            aiCaptures += valueOfPiece(m.getCaptured());
                        }
                    }
                }
            } else {
                playerPieces += valueOfPiece(pc);

                if (aiColor != gameBoard.getTurn())
                {
                    List<Move> validMoves = pc.getValidMoves(gameBoard, true);
                    for(Move m : validMoves) {
                        // account for how many moves can be made
                        playerMoves++;
                        if (m.getCaptured() != null) {
                            // account for possible captures
                            playerCaptures += valueOfPiece(m.getCaptured());
                        }
                    }
                }
            }

        value = (aiPieces - playerPieces) + (aiMoves - playerMoves)
                + (aiCaptures - playerCaptures);

        if (gameBoard.getTurn() == aiColor && aiMoves == 0)
            // if the ai can make no moves, it has lost. this is bad.
            value = Integer.MIN_VALUE;
        else if (gameBoard.getTurn() != aiColor && playerMoves == 0)
            // if the player can make no more moves, we win. this is good.
            value = Integer.MAX_VALUE;
        return value;
    }
    private int valueOfPiece(Piece pc) {
        int pieceValue;
        switch (pc.getImageNumber()) {
            case 0: // Pawn
                pieceValue = 10;
                break;
            case 1: // Knight
                pieceValue = 30;
                break;
            case 2: // Bishop
                pieceValue = 30;
                break;
            case 3: // Rook
                pieceValue = 50;
                break;
            case 4: // Queen
                pieceValue = 90;
                break;
            case 5: // King
                pieceValue = 900;
                break;
            default:
                throw new IllegalArgumentException("Unknown piece type: " + pc.getImageNumber());
        }
        Point location = pc.getLocation();
        int x = location.x;
        int y = location.y;
        int centerBonus = (int) ((7 - Math.abs(x - 3.5)) * (7 - Math.abs(y - 3.5)));

        return pieceValue + centerBonus;
    }

}
