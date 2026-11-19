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

    final Token[][] board; 
    private final Map<Integer, String> tokenSymbols = new HashMap<>(); // Color to emoji

    // Creates an empty board
    public GameBoard(int rows, int columns) {
        // x rows (0->x-1, top to bottom) by y columns (0-y-1, left to right)
        // [row][col] -> board[0][0] is top-left, board[x-1][y-1] is bottom-right
        setNumRows(rows);
        setNumColumns(columns);
        board = new Token[getNumRows()][getNumColumns()];
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

        for (int r=0; r<getNumRows(); r++){
            // int idx = r * getNumColumns();
            for (int c=0; c<getNumColumns(); c++){
                int idx = r*getNumColumns()+c;
                // 1. Top to Bottom
                if (r+3<getNumRows()){
                    out.add(new int[]{idx, idx+getNumColumns(), idx+2*getNumColumns(), idx+3*getNumColumns()});
                }
                // 2. Left to Right (rows)
                if (c+3 < getNumColumns()){
                    out.add(new int[]{idx, idx+1, idx+2, idx+3});
                }
                // 3. Digonal top-left to bottom-right
                if (idx+(getNumColumns()+1)*3 < getNumRows() * getNumColumns()-1){
                    out.add(new int[]{idx, idx+1+getNumColumns(), idx+(1+getNumColumns())*2, idx+(1+getNumColumns())*3});
                }
                // 4. Diagonal Top-right to Bottom-left
                if (c>=3 && idx+(getNumColumns()-1)*3 < getNumRows() * getNumColumns() - 1){
                    out.add(new int[]{idx, idx-1+getNumColumns(), idx+(getNumColumns()-1)*2, idx+(getNumColumns()-1)*3});
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
        for (int row = 0; row < getNumRows(); row++) {
            for (int column = 0; column < getNumColumns(); column++) {
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

        for (int row = getNumRows() - 1; row >= 0; row--) {
            if (board[row][column] == null) {
                board[row][column] = token;
                return row;
            }
        }
        return -1;
    }

    /** Checks that both indices fall within the board. */
    private void checkPosition(int row, int column) {
        if (row < 0 || row >= getNumRows()) {
            throw new IllegalArgumentException("Row must be between 0 and " + Integer.toString(getNumRows()) + ".");
        }
        checkColumn(column);
    }

    /** Checks that a column index falls within the board. */
    private void checkColumn(int column) {
        if (column < 0 || column >= getNumColumns()) {
            throw new IllegalArgumentException("Column must be between 0 and " + Integer.toString(getNumColumns()) + ".");
        }
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
