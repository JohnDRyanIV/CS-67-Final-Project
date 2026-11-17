/**
 * Owns the board and will coordinate game rules
 */
public class GameEngine {
    private GameBoard board = new GameBoard();
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
        board = new GameBoard();
        currentPlayer = Token.RED;
    }

    /**
     * Determines the state of the game: 1 or -1 if one side wins, 0 if it's drawn
     * and null if game game isn't over yet.
     */
    public Integer gameState(){
        // Get the sum of integer values of each window
        for (int[] window: board.windows()){
            int tot = 0;
            for (int idx: window){
                int r = idx / GameBoard.COLUMNS, c = idx % GameBoard.COLUMNS;
                Token tok = board.getToken(r, c);
                if (tok == null){
                    break;
                }
                tot += tok.getTokenValue();

            }

            if (Math.abs(tot) == 4){
                return tot/4;
            }
        }
        // Board not full
        for (int r=0; r<GameBoard.ROWS; r++){
            if (board.getToken(r, 0) == null){
                return null;
            }
        }
        return 0;
    }
}
