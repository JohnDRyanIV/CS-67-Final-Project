/**
 * Owns the board and will coordinate game rules
 */
public class GameEngine {
    private final GameBoard board = new GameBoard();

    /** Returns this game's board for reading cell contents or displaying it. */
    public GameBoard getBoard() {
        return board;
    }

    // TODO: Implement turns, move handling, win/draw detection
}
