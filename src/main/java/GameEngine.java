/**
 * Owns the board, coordinates game rules, and searches for moves.
 */
public class GameEngine {

    private GameBoard board;
    public GameEngine(int rows, int columns, int connect) {
        board = new GameBoard(rows, columns, connect);
    }
 
    private int currentPlayer = Token.RED;

    /** Returns this game's board for reading cell contents or displaying it. */
    public GameBoard getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /** Places the current player's token. A full column does not change turns. */
    public boolean placeToken(int column) {
        if (gameState() != null){
            return false;
        }
        // If token drop is invalid
        if (board.dropToken(column, new Token(currentPlayer)) == -1) {
            return false;
        }
        // Switch active player turn
        if (gameState() == null && currentPlayer == Token.RED) {
            currentPlayer = Token.BLACK;
        } else {
            currentPlayer = Token.RED;
        }
        return true;
    }

    /** Starts a new game with an empty board and red playing first. */
    public void reset() {
        board.clear();
        currentPlayer = Token.RED;
    }

    /**
     * Finds the current player's best column using minimax with alpha-beta pruning.
     *
     * @param depth maximum number of moves to search ahead
     * @return a column, or null if depth is zero or the game is over
     * @throws IllegalArgumentException if depth is negative
     */
    public Integer abSearch(int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Search depth must be nonnegative.");
        }
        if (depth == 0 || gameState() != null) {
            return null;
        }

        GameBoard searchBoard = board.copy();
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double bestScore = Double.NEGATIVE_INFINITY;
        Integer bestColumn = null;

        for (int column = 0; column < searchBoard.getNumColumns(); column++) {
            if (searchBoard.getToken(0, column) != null) {
                continue;
            }

            int row = searchBoard.dropToken(column, new Token(currentPlayer));
            double score;
            try {
                // The opponent chooses the next move, so start with a minimizing turn.
                score = minimax(searchBoard, depth - 1, alpha, beta, currentPlayer, false);
            } finally {
                // Restore the board before trying another column, even if search fails.
                searchBoard.undoDrop(row, column);
            }

            // Keep the first legal column as a fallback even if every move loses.
            if (bestColumn == null) {
                bestColumn = column;
            }
            if (score > bestScore) {
                bestScore = score;
                bestColumn = column;
            }

            alpha = Math.max(alpha, bestScore);
            if (alpha >= beta) {
                break;
            }
        }
        // System.out.println("Best column for player " + currentPlayer + ": " + bestColumn + " with score: " + bestScore);
        return bestColumn;
    }

    /**
     * Scores a position using minimax with alpha-beta pruning.
     *
     * @param position working board for simulated moves
     * @param depth number of moves left to search
     * @param alpha lower score bound for the maximizing player
     * @param beta upper score bound for the minimizing player
     * @param player player whose score is maximized
     * @param maximizing whether the current turn maximizes the score
     * @return the minimax score from the specified player's perspective
     */
    private double minimax(GameBoard position, int depth, double alpha,
            double beta, int player, boolean maximizing) {
        Integer state = gameState(position);
        if (depth == 0 || state != null) {
            return evalBoard(position, state, player);
        }

        double value;
        int movingPlayer;
        if (maximizing) {
            value = Double.NEGATIVE_INFINITY;
            movingPlayer = player;
        } else {
            value = Double.POSITIVE_INFINITY;
            movingPlayer = -player;
        }

        for (int column = 0; column < position.getNumColumns(); column++) {
            // A column is legal when its top cell is empty.
            if (position.getToken(0, column) != null) {
                continue;
            }

            // Reuse the working board and undo this move before trying the next one.
            int row = position.dropToken(column, new Token(movingPlayer));
            double score;
            try {
                score = minimax(position, depth - 1, alpha, beta, player, !maximizing);
            } finally {
                position.undoDrop(row, column);
            }

            if (maximizing) {
                value = Math.max(value, score);
                alpha = Math.max(alpha, value);
            } else {
                value = Math.min(value, score);
                beta = Math.min(beta, value);
            }
            if (alpha >= beta) {
                break; // Remaining moves cannot improve the ancestor's choice.
            }
        }
        return value;
    }

    /**
     * Scores a finished position or delegates to the heuristic.
     *
     * @param position board to evaluate
     * @param state winner's color, 0 for a draw, or null if unfinished
     * @param player player whose perspective determines the score
     * @return the position's score from the specified player's perspective
     */
    private double evalBoard(GameBoard position, Integer state, int player) {
        if (state == null) {
            return scorePosition(position, player);
        }
        if (state == 0) {
            return 0;
        }
        if (state == player) {
            return Double.POSITIVE_INFINITY;
        }
        return Double.NEGATIVE_INFINITY;
    }
    /**
     * Provides a heuristic score for an unfinished position,
     * weighting each cell by how many winning lines it is part of and whether the window is blocked or dominated by one player. 
     * @param position
     * @param player
     * @return
     */

    private double[] weightedWinLines(GameBoard position, int player) {
        double[] weights = new double[position.getNumRows() * position.getNumColumns()+1];
        
        // First determine how valuable each point is
        for (int[] window: position.windows()){
            weights[window[0]] += 1;
        }
        // Then determine who dominates and if the window is blocked
        for (int[] window: position.windows()){
            int side = 0;
            for (int idx: window){
                int r = idx / position.getNumColumns(), c = idx % position.getNumColumns();
                Token tok = position.getToken(r, c);
                if (tok == null){
                    continue;
                }
                if (side != 0 && side != tok.getColor()){
                    // Blocked window, no one can win here
                    weights[window[0]] -= 1;
                    break;
                }
                side = tok.getColor();
            }
            if (side == 0){
                weights[weights.length-1] += 1; // Count the number of windows that are available
                weights[window[0]] *= 0.1 * player; // Empty window is less valuable than a window dominated by one player
            }
            else if (side != player){
                weights[window[0]] *= -player;
            } else {
                weights[weights.length-1] += 1;
            }
        }
        return weights;
    }

    /**
     * Provides a heuristic score for an unfinished position.
     *
     * @param position working board to evaluate
     * @param player player whose perspective determines the score
     * @return a score, with positive values favoring the specified player
     */
    protected double scorePosition(GameBoard position, int player) {
        double[] weights = weightedWinLines(position, player);
        double score = 0;
        for (int i=0; i<weights.length-1; i++){
            score += weights[i];
        }
        // Normalize the score by the number of cells to keep it in a reasonable range
        return score/weights[weights.length-1];
        // NOTE: Why from player's perspective? Shouldn't it be independent of the player?
        // TODO: Penalize columns that result in oppenent gaining massive advantage in next turn
        // This forsees Zugzwang situations where the player is forced to make a move that allows the opponent to win in the next turn.
    }

    /**
     * Determines the state of the game: 1 or -1 if one side wins, 0 if it's drawn
     * and null if game game isn't over yet.
     */
    public Integer gameState(){
        return gameState(board);
    }

    /**
     * Determines whether a position has a winner, is drawn, or is unfinished.
     *
     * @param position board to evaluate
     * @return the winner's color, 0 for a draw, or null if unfinished
     */
    private Integer gameState(GameBoard position){
        // Get the sum of integer values of each window
        for (int[] window: position.windows()){
            int tot = 0;
            for (int idx: window){
                int r = idx / position.getNumColumns(), c = idx % position.getNumColumns();
                Token tok = position.getToken(r, c);
                if (tok == null){
                    break;
                }
                tot += tok.getTokenValue();

            }

            if (Math.abs(tot) == position.getConnect()){
                return tot/position.getConnect();
            }
        }
        // Board not full
        for (int c=0; c<position.getNumColumns(); c++){
            if (position.getToken(0, c) == null){
                return null;
            }
        }
        return 0;
    }
}
