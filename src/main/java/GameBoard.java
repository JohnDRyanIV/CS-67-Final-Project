import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * A Connect Four board with 6 rows and 7 columns. Empty cells contain null.
 * Stores cell contents and provides a console display. Game rules belong in
 * GameEngine.
 */
public class GameBoard {

    public static final int ROWS = 6;
    public static final int COLUMNS = 7;

    final Token[][] board; 
    private final Map<Integer, String> tokenSymbols = new HashMap<>(); // Color to emoji

    // Creates an empty board
    public GameBoard() {
        // 6 rows (0-5, top to bottom) by 7 columns (0-6, left to right)
        // [row][col] -> board[0][0] is top-left, board[5][6] is bottom-right
        board = new Token[ROWS][COLUMNS];
        tokenSymbols.put(Token.BLACK, "⚫");
        tokenSymbols.put(Token.RED, "🔴");
        tokenSymbols.put(null, "⚪");
    }

    /**
     * Get all windows on which to check for wins.
     * This returns an ArrayList of integers that represent the indices on
     * a flattened grid. To convert back to (row, col), (row = x % width, col = x/width)
     */
    public ArrayList<int[]> windows(){
        ArrayList<int[]> out = new ArrayList<>();

        for (int r=0; r<ROWS; r++){
            // int idx = r * COLUMNS;
            for (int c=0; c<COLUMNS; c++){
                int idx = r*COLUMNS+c;
                // 1. Top to Bottom
                if (r+3<ROWS){
                    out.add(new int[]{idx, idx+COLUMNS, idx+2*COLUMNS, idx+3*COLUMNS});
                }
                // 2. Left to Right (rows)
                if (c+3 < COLUMNS){
                    out.add(new int[]{idx, idx+1, idx+2, idx+3});
                }
                // 3. Digonal top-left to bottom-right
                if (idx+(COLUMNS+1)*3 < ROWS * COLUMNS-1){
                    out.add(new int[]{idx, idx+1+COLUMNS, idx+(1+COLUMNS)*2, idx+(1+COLUMNS)*3});
                }
                // 4. Diagonal Top-right to Bottom-left
                if (c>=3 && idx+(COLUMNS-1)*3 < ROWS * COLUMNS - 1){
                    out.add(new int[]{idx, idx-1+COLUMNS, idx+(COLUMNS-1)*2, idx+(COLUMNS-1)*3});
                }
            }
        }
        return out;
    }

    /**
     * Prints board in terminal from top to bottom, with one line per row and no spaces.
     * Black and red tokens use matching circles. Empty cells use white circles.
     */
    public void display() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                // assign token color
                Token token = board[row][column];
                Integer color;
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

    /** Drops a token into the lowest empty cell. Returns its row, or -1 if full. */
    public int dropToken(int column, Token token) {
        checkColumn(column);

        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][column] == null) {
                board[row][column] = token;
                return row;
            }
        }
        return -1;
    }

    /** Checks that both indices fall within the board. */
    private void checkPosition(int row, int column) {
        if (row < 0 || row >= ROWS) {
            throw new IllegalArgumentException("Row must be between 0 and " + Integer.toString(ROWS) + ".");
        }
        checkColumn(column);
    }

    /** Checks that a column index falls within the board. */
    private void checkColumn(int column) {
        if (column < 0 || column >= COLUMNS) {
            throw new IllegalArgumentException("Column must be between 0 and " + Integer.toString(COLUMNS) + ".");
        }
    }
}
