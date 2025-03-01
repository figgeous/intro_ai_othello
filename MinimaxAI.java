import java.util.ArrayList;

/**
 * Based on the pseudocode for the minimax algorithm from pg. 196 of the textbook.
 * 
 * Currently contains print statements for testing/debugging, which can be removed
 * prior to submission.
 */
public class MinimaxAI {
    private int maxDepth;  // The maximum number of ply (half-moves) to search
    
    public MinimaxAI() {
        this(4);
    }
    
    /**
     * @param maxDepth The maximum depth to search in the game tree.
     */
    public MinimaxAI(int maxDepth) {
        this.maxDepth = maxDepth;
    }
    
    /**
     * MINIMAX-SEARCH in the pseudocode.
     * @param state
     * @return A Pair containing the best move and its score.
     */
    public Pair findBestMove(GameState state) {
        System.out.println("\nLegal moves: " + state.legalMoves());
        
        Pair result = maxValue(state, 0);
        
        System.out.println("\nChosen move: " + result.move + " with minimax value: " + result.score);
        System.out.println("----------------------------------------");
        
        return result;
    }
    
    /**
     * MAX-VALUE function from the pseudocode.
     * @param state
     * @param depth
     * @return A Pair containing the best move and its score.
     */
    private Pair maxValue(GameState state, int depth) {
        if (isTerminal(state, depth)) {
            // If the game is over, return the utility value of the state
            return new Pair(null, utility(state));
        }
        
        ArrayList<Position> actions = state.legalMoves();
        
        if (actions.isEmpty()) {
            // If no legal moves, pass turn to opponent
            System.out.println("No legal moves available for MAX. Passing turn.");
            GameState nextState = new GameState(state.getBoard(), state.getPlayerInTurn());
            nextState.changePlayer();
            return minValue(nextState, depth + 1);
        }
        
        int v = Integer.MIN_VALUE; // like minus infinity
        Position move = null;
        
        System.out.println("\nEvaluating moves for player " + (state.getPlayerInTurn() == 1 ? "Black" : "White") + " (MAX):");
        
        for (Position a : actions) {
            GameState nextState = result(state, a);
            
            Pair minValueResult = minValue(nextState, depth + 1);
            
            System.out.println("Move " + a + " has minimax value: " + minValueResult.score);
            
            if (minValueResult.score > v) {
                v = minValueResult.score;
                move = a;
            }
        }
        
        return new Pair(move, v);
    }
    
    /**
     * MIN-VALUE function from the pseudocode.
     * @param state
     * @param depth
     * @return A Pair containing the best move and its score.
     */
    private Pair minValue(GameState state, int depth) {
        if (isTerminal(state, depth)) {
            // If no legal moves, pass turn to opponent
            return new Pair(null, utility(state));
        }
        
        ArrayList<Position> actions = state.legalMoves();
        
        if (actions.isEmpty()) {
            // If no legal moves, pass turn to opponent
            System.out.println("No legal moves available for MIN. Passing turn.");
            GameState nextState = new GameState(state.getBoard(), state.getPlayerInTurn());
            nextState.changePlayer();
            return maxValue(nextState, depth + 1);
        }
        
        int v = Integer.MAX_VALUE; // like plus infinity
        Position move = null;
        
        for (Position a : actions) {
            GameState nextState = result(state, a);
            
            Pair maxValueResult = maxValue(nextState, depth + 1);
            
            if (maxValueResult.score < v) {
                v = maxValueResult.score;
                move = a;
            }
        }
        
        return new Pair(move, v);
    }
    
    /**
     * Is the game over or have we reached the max depth?
     * @param state
     * @param depth
     * @return True if terminal, false otherwise.
     */
    private boolean isTerminal(GameState state, int depth) {
        return depth >= maxDepth || state.isFinished();
    }
    
    /**
     * Returns the utility value of a state.
     * @param state
     * @return The utility value.
     */
    private int utility(GameState state) {
        return evaluateBoard(state);
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
        int[] tokens = state.countTokens();
        return tokens[0] - tokens[1]; // Black tokens - White tokens
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