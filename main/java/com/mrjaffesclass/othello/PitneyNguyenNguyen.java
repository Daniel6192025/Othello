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
     * Gets the player color
     * @return        PitneyNguyenNguyen color
     */
    public int getColor() {
        return this.color;
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
    @Override
    public Position getNextMove(Board board) {
        ArrayList<Position> legalMoves = getLegalMoves(board);

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

    private int minimax(Board board, Position position, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.countSquares(Constants.EMPTY) > 0) {
            //returns final move at the end of depth (or moves in the future)
            return evaluate(board, position);
        }

        if (maximizingPlayer) {
            int maxEval = -1000; // default
            ArrayList<Position> list = this.getLegalMoves(board);
            //gets a list of moves we can make
            for (Position pos:list) {
                // recursion function to find opponents worst moves (?)
                int eval = minimax(board, pos, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval); // determines which evaluation of position is better
                alpha = Math.max(alpha, eval); //sets alpha as best evaluated move
                if (beta <= alpha) {
                    break; 
                }
            }
            return maxEval;
        } else {
            int minEval = 1000;
            ArrayList<Position> list = this.getLegalMoves(board); // change this later to get opponents move somehow
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

    private int evaluate(Board board, Position position) {
        int mobility, edge, flippedDiscs = 0, opponentMoves = 0;
        //mobility == number of moves available by the end of it
        // edge = number of edges pieces take (by two)
        // corner pieces taken
        // stability = number of our color pieces without white surrounding them
        // opponent moves = number of moves opponent has after our move
        int color = this.getColor();
        
        //Opponent moves counter
        opponentMoves = getOpponentLegalMoves(board).size();
        
        //Mobility
        List<Position> mobilityList = getLegalMoves(board);
        mobility = mobilityList.size();
        
        //Edge count
        int edgeCount = 0;
        ArrayList<Position> listOne = new ArrayList<>();
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position testPos = new Position(row, col);
                if (row == 0 || row == 7 || row == 6 || row == 1 || col == 0 || col == 7 || col == 6 || col == 1) {
                    if (board.getSquare(testPos).getStatus() == this.color) {
                        edgeCount++;
                    }
                }
            }
        }
        
        //Number of flipped discs
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position testPos = new Position(row, col);
                if (board.getSquare(testPos).getStatus() == this.color) {
                    flippedDiscs++;
                }
            }
        }

        int totalScore = (edgeCount + flippedDiscs + mobility) - opponentMoves;
        
        return totalScore;
    } 

    public ArrayList<Position> getPieces(Board board) {
        int color = getColor();
        ArrayList<Position> list = new ArrayList<>();
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position testPos = new Position(row, col);
                if (board.getSquare(testPos).getStatus() == color) {
                    list.add(testPos);
                }
            }
        }
        return list;
    }

    private Board copyBoard(Board originalBoard) {
        Board copiedBoard = new Board();

        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position pos = new Position(row, col);
                int status = originalBoard.getSquare(pos).getStatus();
                copiedBoard.getSquare(pos).setStatus(status);
            }
        }
        return copiedBoard;
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
                    if (board.isLegalMove(this, testPosition)) {
                        list.add(testPosition);
                    }
                }        
            }
        }
        return list;
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
        for (Position pos : getOpponentPositions(board)) {
            for (String dir : Directions.getDirections()) {
                Position directionVector = Directions.getVector(dir);
                Position newPos = pos.translate(directionVector);

                if (!newPos.isOffBoard() && board.getSquare(newPos).getStatus() == Constants.EMPTY) {
                    list.add(newPos);
                }
            }
        }
        return list;
    }

    private boolean step(Board board, Position position, Position direction, int count) {
        Position newPosition = position.translate(direction);
        int color = this.getColor();
        if (newPosition.isOffBoard()) {
            return false;
        } else if (board.getSquare(newPosition).getStatus() == -color) {
            return this.step(board, newPosition, direction, count + 1);
        } else if (board.getSquare(newPosition).getStatus() == color) {
            return count > 0;
        } else {
            return false;
        }
    }
}
