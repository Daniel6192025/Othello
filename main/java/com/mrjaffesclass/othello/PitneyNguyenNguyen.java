package main.java.com.mrjaffesclass.othello;
import java.util.ArrayList;
import java.util.*;
/**
 * PitneyNguyenNguyen object. Students will extend this class
 * and override the getNextMove method
 * 
 * @author Mr. Jaffe
 * @version 1.0
 */
public class PitneyNguyenNguyen extends Player
{
    private final int color = this.color;
    /**
     * PitneyNguyenNguyen constructor
     * @param color   One of Constants.WHITE or Constants.BLACK
     */
    public PitneyNguyenNguyen(int color) {
        super(color);
    }

    /**
     * Gets the player name
     * @return        PitneyNguyenNguyen name
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * The player must override getNextMove
     * @param board Game board
     * @return A position coordinate pair of his/her next move. Returns null
     *          if no move is available
     */
    Position getNextMove(Board board) {
        ArrayList<Position> legalMoves = this.getLegalMoves(board);
        if (legalMoves.isEmpty()) {
            return null;    
        }

        int maxEval = Integer.MIN_VALUE;
        Position bestMove = null;

        for (Position move : legalMoves) {
            int eval = minimax(board, move, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (eval > maxEval) {
                maxEval = eval;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private boolean evaluation(Board board, Position position, int alpha, int beta){
        return true;
    }

    private int minimax(Board board, Position position, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.countSquares(Constants.EMPTY) == 0) {
            // Khoa returning a position fix in progress lol
            return assessMove(board, position);
        }

        if (maximizingPlayer) {
            int maxEval = -1000;
            ArrayList<Position> list = this.getLegalMoves(board);
            for (Position pos:list) {
                int eval = minimax(board, pos, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = 1000;
            ArrayList<Position> list = this.getLegalMoves(board);
            for (Position pos:list) {
                int eval = minimax(board, pos, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }

    }

    private int assessMove(Board board, Position move) {
        int flippedDiscs = countFlippedDiscs(board, move);
        int boardControl = calculateBoardControl(board, move);

        return (2 * flippedDiscs) + boardControl;
    }

    private int calculateBoardControl(Board board, Position move) {
        int control = 0;

        for (String direction : Directions.getDirections()) {
            Position directionVector = Directions.getVector(direction);
            Position newPosition = move.translate(directionVector);
            if (!newPosition.isOffBoard()) {
                if (board.getSquare(newPosition).getStatus() == Constants.EMPTY) {
                    control++;
                }
            }
        }

        return control;
    }

    private int countFlippedDiscs(Board board, Position move) {
        int color = this.getColor();
        int flippedDiscs = 0;

        // Iterate through directions
        for (String direction : Directions.getDirections()) {
            Position directionVector = Directions.getVector(direction);
            Position newPosition = move.translate(directionVector);

            // Check if the new position is on the board
            if (!newPosition.isOffBoard()) {
                // Check if there is a line of opponent discs to flip
                if (step(board, newPosition, directionVector, 0)) {
                    flippedDiscs++;
                }
            }
        }

        return flippedDiscs;
    }

    public ArrayList<Position> getOpponentPositions(Board board) {
        int opponentColor = (this.getColor() == Constants.BLACK) ? Constants.WHITE : Constants.BLACK;
        ArrayList<Position> list = new ArrayList<>();
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position testPosition = new Position(row, col);
                if (board.getSquare(testPosition).getStatus() == opponentColor) {
                    if (board.isLegalMove(this, testPosition)) {
                        list.add(testPosition);
                    }
                }
            }
        }
        return list;
    }

    public ArrayList<Position> getOpponentLegalMoves(Board board) {
        int opponentColor = (this.getColor() == Constants.BLACK) ? Constants.WHITE : Constants.BLACK;
        ArrayList list = new ArrayList<>();
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position testPosition = new Position(row, col);
                if (board.getSquare(testPosition).getStatus() == Constants.EMPTY) {

                    if (board.isLegalMove(this, testPosition)) {
                        list.add(testPosition);
                    }
                }        
            }
        }
        return list;
    }

    /**
     * Are this player and the passed-in player the same?
     * @param p Passed-in player
     * @return true if the players are the same
     */
    boolean isThisPlayer(PitneyNguyenNguyen p) {
        return p.getColor() == this.getColor();
    }

    /**
     * Are this color and the passed-in color the same?
     * @param p Passed-in color (represented as an integer)
     * @return true if the colors (integers) are the same
     */
    boolean isThisPlayer(int p) {
        return p == this.getColor();
    }

    @Override
    public String toString() {
        switch (this.color) {
            case Constants.BLACK:
                return this.getName()+" (BLACK)";
            case Constants.WHITE:
                return this.getName()+" (WHITE)";
            default:
                return this.getName()+" (?????)";
        }
    }

    // from test player class
    private boolean step(Board board, Position position, Position direction, int count) {
        Position newPosition = position.translate(direction);
        int color = this.getColor();
        if (newPosition.isOffBoard()) {
            return false;
        } else if (board.getSquare(newPosition).getStatus() == -color) {
            return this.step(board, newPosition, direction, count+1);
        } else if (board.getSquare(newPosition).getStatus() == color) {
            return count > 0;
        } else {
            return false;
        }
    }

    private boolean isLegalMove(Board board, Position positionToCheck) {
        for (String direction : Directions.getDirections()) {
            Position directionVector = Directions.getVector(direction);
            if (step(board, positionToCheck, directionVector, 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the legal moves for this player on the board
     * @param board
     * @return True if this is a legal move for the player
     */
    public ArrayList<Position> getLegalMoves(Board board) {
        int color = this.getColor();
        ArrayList list = new ArrayList<>();
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                if (board.getSquare(this, row, col).getStatus() == Constants.EMPTY) {
                    Position testPosition = new Position(row, col);
                    if (this.isLegalMove(board, testPosition)) {
                        list.add(testPosition);
                    }
                }        
            }
        }
        return list;
    }
}

