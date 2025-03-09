package pp; //pp = Python programmers
import java.util.ArrayList;

import Provided.GameState;
import provided.*;

/**
 * Based on the pseudocode for the minimax algorithm from pg. 196 of the textbook.
 * 
 * Currently contains print statements for testing/debugging, which can be removed
 * prior to submission.
 */
public class MinimaxAI {
    private int searchDepth;  // The maximum number of ply (half-moves) to search
    
  // Weighted board positioning for the evaluation function
  private static final int[][] POSITION_WEIGHTS = {
    {50, -10, 5, 2, 2, 5, -10, 50},       // Row 0
    {-10, -25, -1, -1, -1, -1, -25, -10}  // Row 1
    {5, -1, 1, 1, 1, 1, -1, 5},           // Row 2
    {2, -1, 1, 1, 1, 1, -1, 2},           // Row 3
    {2, -1, 1, 1, 1, 1, -1, 2},           // Row 4
    {5, -1, 1, 1, 1, 1, -1, 5},           // Row 5
    {-10, -25, -1, -1, -1, -1, -25, -10}, // Row 6
    {50, -10, 5, 2, 2, 5, -10, 50}        // Row 7
   };

    public MinimaxAI() {
        this(4);
    }
    
    /**
     * @param searchDepth The maximum depth to search in the game tree.
     */
    public MinimaxAI(int searchDepth) {
        this.searchDepth = searchDepth;
    }
    
    /**
     * MINIMAX-SEARCH in the pseudocode.
     * @param state
     * @return A Pair containing the best move and its score.
     */
    public Pair findBestMove(GameState state, int player) {
        // System.out.println("\nLegal moves: " + state.legalMoves());
        
        if(player == 1) //we are black
            return maxValue(state, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        else if(player == 2)//we are white
            return minValue(state, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        else
            return new Pair(null, 0);
        // System.out.println("\nChosen move: " + result.move + " with minimax value: " + result.score);
        // System.out.println("----------------------------------------");
    }
    
    /**
     * Is the game over?
     * @param state
     * @return True if terminal, false otherwise.
     */
    private boolean isTerminal(GameState state) {
        return state.isFinished();
    }
    
    /**
     * Returns the utility value of a terminal state.
     * @param state
     * @return The utility value.
     */
    private int utility(GameState state) {
        return evaluateBoard(state);
    }
    
    /**
     * MAX-VALUE function from the pseudocode.
     * @param state
     * @param remainingDepth The remaining depth to search
     * @return A Pair containing the best move and its score.
     */
    private Pair maxValue(GameState state, int remainingDepth, int alpha, int beta) {
        if (isTerminal(state)) {
            // If the game is over, return the utility value of the state
            return new Pair(null, utility(state)); // Simplified: use utility directly
        }
        
        if (remainingDepth <= 0) {
            // If we've reached the depth limit, use the evaluation function
            return new Pair(null, utility(state)); // Simplified: use evaluateBoard directly
        }
        
        ArrayList<Position> actions = state.legalMoves();
        
        if (actions.isEmpty()) {
            // If no legal moves, pass turn to opponent
            //System.out.println("No legal moves available for MAX. Passing turn.");
            state.changePlayer();
            return minValue(state, remainingDepth - 1, alpha, beta);
        }
        
        int v = Integer.MIN_VALUE; // like minus infinity
        Position move = null;
        
        //System.out.println("\nEvaluating moves for player " + (state.getPlayerInTurn() == 1 ? "Black" : "White") + " (MAX):");
        
        for (Position a : actions) {
            GameState nextState = result(state, a);
            
            Pair minValueResult = minValue(nextState, remainingDepth - 1, alpha, beta);
            
            //System.out.println("Move " + a + " has minimax value: " + minValueResult.score);
            
            if (minValueResult.score > v) {
                v = minValueResult.score;
                move = a;
                alpha = Math.max(alpha, v);
            }
            if (v >= beta){
                return new Pair(move, v);
            }
        }
        
        return new Pair(move, v);
    }
    
    /**
     * MIN-VALUE function from the pseudocode.
     * @param state
     * @param remainingDepth The remaining depth to search
     * @return A Pair containing the best move and its score.
     */
    private Pair minValue(GameState state, int remainingDepth,int alpha, int beta) {
        if (isTerminal(state)) {
            // If the game is over, return the utility value of the state
            return new Pair(null, utility(state));
        }
        
        if (remainingDepth <= 0) {
            // If we've reached the depth limit, use the evaluation function
            return new Pair(null, evaluateBoard(state));
        }
        
        ArrayList<Position> actions = state.legalMoves();
        
        if (actions.isEmpty()) {
            // If no legal moves, pass turn to opponent
            System.out.println("No legal moves available for MIN. Passing turn.");
            state.changePlayer();
            return maxValue(state, remainingDepth - 1, alpha, beta);
        }
        
        int v = Integer.MAX_VALUE; // like plus infinity
        Position move = null;
        
        for (Position a : actions) {
            GameState nextState = result(state, a);
            
            Pair maxValueResult = maxValue(nextState, remainingDepth - 1, alpha, beta);
            
            if (maxValueResult.score < v) {
                v = maxValueResult.score;
                move = a;
                beta = Math.min(beta, v);
            }
            if(v <= alpha){
                return new Pair(move, v);
            }
        }
        
        return new Pair(move, v);
    }
    
    /**
     * Returns the result of applying some action to the current state.
     * @param state The current game state.
     * @param action The action to apply.
     * @return The resulting game state.
     */
    private GameState result(GameState state, Position action) {
        GameState nextState = new GameState(state.getBoard(), state.getPlayerInTurn());
        nextState.insertToken(action);
        return nextState;
    }
    
    /**
     * Evaluates the board state and returns a score.
     * Higher scores favour MAX player (black). For the moment this the difference
     * between the number of black and white tokens on the board.
     * @param state
     * @return The score of the board.
     */
    private int evaluateBoard(GameState state) {
        int[] [] board = state.getBoard();
        int size = board.length;
        int score = 0;
        
        // Evaluate the board based on piece count and position
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 1) { // Black pieces (MAX)
                    score += 1; // Each piece is worth 1 point
                    score += POSITION_WEIGHTS[i][j]; // Add position-based weight
                } else if (board[i][j] == 2) { // White pieces (MIN)
                    score -= 1; // Each opponent piece is worth -1 point
                    score -= POSITION_WEIGHTS[i][j]; // Subtract position-based weight
                }
            }
        }

         // To print the final score difference for debugging and performance tracking
         System.out.println("Final Score Difference: " + score);
        
        return score;
    }
    
    /**
     * Helper class for a move and its score.
     */
    public static class Pair {
        public Position move;
        public int score;
        
        public Pair(Position move, int score) {
            this.move = move;
            this.score = score;
        }
    }
} 