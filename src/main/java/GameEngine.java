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
        // If token drop is invalid
        if (board.dropToken(column, new Token(currentPlayer)) == -1) {
            return false;
        }
        // Switch active player turn
        if (currentPlayer == Token.RED) {
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

    // TODO: Win/draw detection, AI implementation
}
