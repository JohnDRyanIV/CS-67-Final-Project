import java.util.HashMap;
import java.util.Map;

/**
 * A Connect Four board with 6 rows and 7 columns. Empty cells contain null.
 * Stores cell contents and provides a console display. Game rules belong in
 * GameEngine, future GUI can read cell contents through getToken().
 */
public class GameBoard {

    public static final int ROWS = 6;
    public static final int COLUMNS = 7;

    final Token[][] board; 
    private final Map<Character, String> tokenSymbols = new HashMap<>(); // Color to emoji

    // Creates an empty board
    public GameBoard() {
        // 6 rows (0-5, top to bottom) by 7 columns (0-6, left to right)
        // [row][col] -> board[0][0] is top-left, board[5][6] is bottom-right
        board = new Token[ROWS][COLUMNS];
        tokenSymbols.put('b', "⚫");
        tokenSymbols.put('r', "🔴");
        tokenSymbols.put(null, "⚪");
    }

    /**
     * Prints the board from top to bottom, with one line per row and no spaces.
     * Black and red tokens use matching circles; empty cells use white circles.
     */
    public void display() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                // assign token color
                Token token = board[row][column];
                Character color;
                if(token == null)
                    color = null;
                else
                    color = token.getColor();
                System.out.print(tokenSymbols.get(color));
            }
            System.out.println();
        }
    }

    /**
     * Returns the token in a cell, or null if the cell is empty.
     * Check for null before accessing or changing the returned token.
     * @param row row index, from 0 (top) to 5 (bottom)
     * @param column column index, from 0 (left) to 6 (right)
     * @return the token at the requested position, or null for an empty cell
     * @throws IllegalArgumentException if either index is outside the board
     */
    public Token getToken(int row, int column) {
        checkPosition(row, column);
        return board[row][column];
    }

    /** Checks that both indices fall within the board. */
    private void checkPosition(int row, int column) {
        if (row < 0 || row >= ROWS) {
            throw new IllegalArgumentException("Row must be between 0 and 5.");
        }
        checkColumn(column);
    }

    /** Checks that a column index falls within the board. */
    private void checkColumn(int column) {
        if (column < 0 || column >= COLUMNS) {
            throw new IllegalArgumentException("Column must be between 0 and 6.");
        }
    }
}
