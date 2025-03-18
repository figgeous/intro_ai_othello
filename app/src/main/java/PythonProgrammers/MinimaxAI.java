package PythonProgrammers;
import java.util.ArrayList;
import Provided.Position;
import Provided.GameState;

/**
 * Based on the pseudocode for the minimax algorithm from pg. 196 of the textbook.
 * 
 * Currently contains print statements for testing/debugging, which can be removed
 * prior to submission.
 */
public class MinimaxAI {
    private int searchDepth;  // The maximum number of ply (half-moves) to search
    private boolean hasPrintedResult = false;

    public MinimaxAI() {
        this(6);
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
        Pair result;
        if(player == 1) //we are black
            result = maxValue(state, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        else if(player == 2)//we are white
            result =  minValue(state, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        else
            result =  new Pair(null, 0);
            return result;
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
     * @param state The current game state.
     * @return The utility value.
     */
    private int utility(GameState state) {
        if (!hasPrintedResult) {
            printEvaluationDetails(state);
            hasPrintedResult = true; // Ensure results are printed only once
        }
        return evaluateBoard(state);
    }

    
    /**
     * MAX-VALUE function from the pseudocode.
     * @param state
     * @param remainingDepth The remaining depth to search
     * @return A Pair containing the best move and its score.
     */
    private Pair maxValue(GameState state, int remainingDepth, int alpha, int beta, boolean isRoot) {
        if (isTerminal(state)) {
            // If the game is over, return the utility value of the state
            return new Pair(null, utility(state)); // Simplified: use utility directly
        }
        
        if (remainingDepth <= 0) {
            // If we've reached the depth limit, use the evaluation function
            return new Pair(null, evaluateBoard(state)); // Simplified: use evaluateBoard directly
        }
        
        ArrayList<Position> actions = state.legalMoves();
        
        if (actions.isEmpty()) {
            // If no legal moves, pass turn to opponent
            //System.out.println("No legal moves available for MAX. Passing turn.");
            state.changePlayer();
            return minValue(state, remainingDepth - 1, alpha, beta, false);
        }
        
        int v = Integer.MIN_VALUE; // like minus infinity
        Position move = null;
        
        //System.out.println("\nEvaluating moves for player " + (state.getPlayerInTurn() == 1 ? "Black" : "White") + " (MAX):");
        
        for (Position a : actions) {
            GameState nextState = result(state, a);
            Pair minValueResult = minValue(nextState, remainingDepth - 1, alpha, beta, false);
            
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
     private Pair minValue(GameState state, int remainingDepth, int alpha, int beta, boolean isRoot) {
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
            return maxValue(state, remainingDepth - 1, alpha, beta, false);
        }
        
        int v = Integer.MAX_VALUE; // like plus infinity
        Position move = null;
        
        for (Position a : actions) {
            GameState nextState = result(state, a);
            
            Pair maxValueResult = maxValue(nextState, remainingDepth - 1, alpha, beta, false);
            
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
        // Count tokens
        int[] tokens = state.countTokens();
        int blackTokens = tokens[0];
        int whiteTokens = tokens[1];

        // Count corners
        int blackCorners = countCornerTokens(state, 1); // Count corners for Black
        int whiteCorners = countCornerTokens(state, 2); // Count corners for White

        // Count edges
        int blackEdges = countEdgeTokens(state, 1); // Count edges for Black
        int whiteEdges = countEdgeTokens(state, 2); // Count edges for White

        // Count mobility (number of legal moves)
        int blackMobility = state.legalMoves().size();
        state.changePlayer(); // Switch to the other player
        int whiteMobility = state.legalMoves().size();
        state.changePlayer(); // Switch back to the original player

        // Weighted evaluation
        int score = (blackTokens - whiteTokens) + // Token difference
                    (8 * (blackCorners - whiteCorners)) + // Corners are very valuable
                    (5 * (blackEdges - whiteEdges)) + // Edges are moderately valuable
                    (3 * (blackMobility - whiteMobility)); // Mobility is somewhat valuable

        return score;
    }

    /**
     * Counts the number of tokens in the corners for a given player.
     * @param state The current game state.
     * @param player The player (1 for Black, 2 for White).
     * @return The number of corners controlled by the player.
     */
    private int countCornerTokens(GameState state, int player) {
        int[][] board = state.getBoard();
        int size = board.length;
        int corners = 0;

        // Check the four corners
        if (board[0][0] == player) corners++;
        if (board[0][size - 1] == player) corners++;
        if (board[size - 1][0] == player) corners++;
        if (board[size - 1][size - 1] == player) corners++;

        return corners;
    }

    /**
     * Counts the number of tokens on the edges for a given player.
     * @param state The current game state.
     * @param player The player (1 for Black, 2 for White).
     * @return The number of edges controlled by the player.
     */
    private int countEdgeTokens(GameState state, int player) {
        int[][] board = state.getBoard();
        int size = board.length;
        int edges = 0;

        // Check the top and bottom edges
        for (int i = 0; i < size; i++) {
            if (board[0][i] == player) edges++;
            if (board[size - 1][i] == player) edges++;
        }

        // Check the left and right edges (excluding corners to avoid double-counting)
        for (int i = 1; i < size - 1; i++) {
            if (board[i][0] == player) edges++;
            if (board[i][size - 1] == player) edges++;
        }

        return edges;
    }

    /**
     * Prints the final results (Black and White tokens, token difference, and the winner).
     * @param state The current game state.
     */
    private void printEvaluationDetails(GameState state) {
        int[] tokens = state.countTokens();
        int blackTokens = tokens[0];
        int whiteTokens = tokens[1];

        // Print winner summary 
        System.out.println("Black tokens: " + blackTokens);
        System.out.println("White tokens: " + whiteTokens);
        System.out.println("Token difference: " + (blackTokens - whiteTokens));

        if (blackTokens > whiteTokens) {
            System.out.println("Black wins!");
        } else if (whiteTokens > blackTokens) {
            System.out.println("White wins!");
        } else {
            System.out.println("It's a tie!");
        }
        System.out.println("----------------------------------------");
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