import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A Connect Four board with a user-defined number of rows and columns. Empty cells 
 * contain null. Stores cell contents and provides a console display. Game rules
 * belong in GameEngine.
 */
public class GameBoard {

    private int rows;
    private int columns;
    private int connect; // Number of tokens in a row to win

    private Token[][] board; 
    private final Map<Integer, String> tokenSymbols = new HashMap<>(); // Color to emoji

    // Creates an empty board
    public GameBoard(int rows, int columns, int connect) {
        // x rows (0->x-1, top to bottom) by y columns (0-y-1, left to right)
        // [row][col] -> board[0][0] is top-left, board[x-1][y-1] is bottom-right
        setNumRows(rows);
        setNumColumns(columns);
        this.connect = connect;
        board = new Token[rows][columns];
        tokenSymbols.put(Token.BLACK, "⚫");
        tokenSymbols.put(Token.RED, "🔴");
        tokenSymbols.put(null, "⚪");
    }

    public void clear(){
        board = new Token[rows][columns];
    }

    /**
     * Get all windows on which to check for wins.
     * This returns an ArrayList of integers that represent the indices on
     * a flattened grid. To convert back to (row, col), (row = x % width, col = x/width)
     */
    public ArrayList<int[]> windows(){
        ArrayList<int[]> out = new ArrayList<>();
        int offset = connect-1;
        // 1. Vertical wins
        for (int c=0; c<columns; c++){
            for (int r=0; r<rows-offset; r++){
                int idx = r*columns+c;
                int[] window = new int[connect];
                for (int i=0; i<connect; i++){
                    window[i] = idx + i*columns;
                }
                out.add(window);
            }
        }
        // 2. Horizontal wins
        for (int r=0; r<rows; r++){
            for (int c=0; c<columns-offset; c++){
                int idx = r*columns+c;
                int[] window = new int[connect];
                for (int i=0; i<connect; i++){
                    window[i] = idx + i;
                }
                out.add(window);
            }
        }
        // 3. Diagonal wins
        for (int r=0; r<rows-offset; r++){
            for (int c=0; c<columns-offset; c++){
                int idx = r*columns+c;
                int[] window = new int[connect];
                for (int i=0; i<connect; i++){
                    window[i] = idx + i + i*columns;
                }
                out.add(window);
            }
        }
        for (int r=0; r<rows-offset; r++){
            for (int c=offset; c<columns; c++){
                int idx = r*columns+c;
                int[] window = new int[connect];
                for (int i=0; i<connect; i++){
                    window[i] = idx - i + i*columns;
                }
                out.add(window);
            }
        }

        return out;
    }

    /**
     * Prints board in terminal from top to bottom, with one line per row and no spaces.
     * Black and red tokens use matching circles. Empty cells use white circles.
     */
    public void display() {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
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
     * @param row row index, from 0 (top) to x-1 (bottom)
     * @param column column index, from 0 (left) to y-1 (right)
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

        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][column] == null) {
                board[row][column] = token;
                return row;
            }
        }
        return -1;
    }

    /** Checks that both indices fall within the board. */
    private void checkPosition(int row, int column) {
        if (row < 0 || row >= rows) {
            throw new IllegalArgumentException("Row must be between 0 and " + Integer.toString(rows) + ".");
        }
        checkColumn(column);
    }

    /** Checks that a column index falls within the board. */
    private void checkColumn(int column) {
        if (column < 0 || column >= columns) {
            throw new IllegalArgumentException("Column must be between 0 and " + Integer.toString(columns) + ".");
        }
    }

    public int getConnect() {
        return this.connect;
    }

    public void setConnect(int connect) {
        this.connect = connect;
    }

    public int getNumRows() {
        return this.rows;
    }

    private void setNumRows(int rows) {
        this.rows = rows;
    }

    public int getNumColumns() {
        return this.columns;
    }

    private void setNumColumns(int columns) {
        this.columns = columns;
    }
}
