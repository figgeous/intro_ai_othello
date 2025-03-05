package pp;
import provided.*;
/**
 * Implementation of our bot. Uses the minimax algorithm to make decisions.
 */
public class OurBot implements IOthelloAI {
    private MinimaxAI minimaxAI;
    
    /**
     * @param searchDepth The maximum depth to search in the game tree.
     */
    public OurBot(int searchDepth) {
        this.minimaxAI = new MinimaxAI(searchDepth);
    }
    
    public OurBot() {
        this(6); //  4 ply is two full moves
    }
    
    /**
     * @param state
     * @return The position where the bot wants to put its token.
     */
    @Override
    public Position decideMove(GameState state) {
        MinimaxAI.Pair bestMove = minimaxAI.findBestMove(state, state.getPlayerInTurn());
        return bestMove.move;
    }
} 