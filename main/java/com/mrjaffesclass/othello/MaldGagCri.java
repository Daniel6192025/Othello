package main.java.com.mrjaffesclass.othello;

/*
 * A player for testing purposes
 * Copyright 2017 Roger Jaffe
 * All rights reserved
 */

import java.util.ArrayList;
/**
 * Test player
 */
public class MaldGagCri extends Player {

    /**
     * Constructor
     * @param name Player's name
     * @param color Player color: one of Constants.BLACK or Constants.WHITE
     */
    public MaldGagCri(int color) {
        super(color);
    }

    int depth = 22;
    static int minimax(int depth, int nodeIndex, boolean Max,
    ArrayList<Integer> moves, int md)
    {
        // Terminating condition. i.e leaf node is reached
        if (depth == md)
            return moves.get(nodeIndex);

        // If current move is maximizer, find the maximum attainable
        // value
        if (Max)
            return Math.max(minimax(depth+1, nodeIndex*2, false, moves, md),
                minimax(depth+1, nodeIndex*2 + 1, false, moves, md));

        // Else (If current move is Minimizer), find the minimum
        // attainable value
        else
            return Math.min(minimax(depth+1, nodeIndex*2, true, moves, md),
                minimax(depth+1, nodeIndex*2 + 1, true, moves, md));
    }

    /**
     *
     * @param board
     * @return The player's next move
     */
    @Override
    public Position getNextMove(Board board) {
        ArrayList<Position> list = this.getLegalMoves(board);
        if (list.size() > 0) {
            int idx = minimax(0, 1, false, moves, 4);
            return list.get(idx);
        } else {
            return null;
        }
    }
    // Returns the optimal value a maximizer can obtain.
    // depth is current depth in game tree.
    // nodeIndex is index of current node in scores[].
    // isMax is true if current move is of maximizer, else false
    // scores[] stores leaves of Game tree.
    // md is maximum height of ma dic

    /**
     * Is this a legal move?
     * @param player Player asking
     * @param positionToCheck Position of the move being checked
     * @return True if this space is a legal move
     */
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
     * Traverses the board in the provided direction. Checks the status of
     * each space: 
     * a. If it's the opposing player then we'll move to the next
     *    space to see if there's a blank space
     * b. If it's the same player then this direction doesn't represent
     *    a legal move
     * c. If it's a blank AND if it's not the adjacent square then this
     *    direction is a legal move. Otherwise, it's not.
     * 
     * @param player  Player making the request
     * @param position Position being checked
     * @param direction Direction to move
     * @param count Number of steps we've made so far
     * @return True if we find a legal move
     */
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
