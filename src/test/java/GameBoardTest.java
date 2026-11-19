

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GameBoardTest {

    public static int countWinningLines(int rows, int cols, int connect) {
        // `int connect` is a safe parameter in case the game gets changed to a different number of tokens in a row to win.
        int horizontal = rows * Math.max(0, cols - connect + 1);

        int vertical = cols * Math.max(0, rows - connect + 1);

        int diagonal = Math.max(0, rows - connect + 1)
                    * Math.max(0, cols - connect + 1);

        return horizontal + vertical + 2 * diagonal;
    }

    @Test
    public void correctNumberOfWinLines() {
        assertEquals(countWinningLines(6, 7, 4), new GameBoard(6, 7, 4).windows().size());
        assertEquals(countWinningLines(8, 8, 4), new GameBoard(8, 8, 4).windows().size());
        assertEquals(countWinningLines(10, 10, 4), new GameBoard(10, 10, 4).windows().size());
        
        // Connect variants
        assertEquals(countWinningLines(6, 7, 3), new GameBoard(6, 7, 3).windows().size());
        assertEquals(countWinningLines(6, 7, 5), new GameBoard(6, 7, 5).windows().size());
        assertEquals(countWinningLines(8, 8, 6), new GameBoard(8, 8, 6).windows().size());
    }

    @Test
    public void correctWindowSizes() {
        // GameBoard board = new GameBoard(6, 7, 4);
        for (int[] window : new GameBoard(6, 7, 4).windows()) {
            assertEquals(4, window.length);
        }
        for (int[] window : new GameBoard(8, 8, 5).windows()) {
            assertEquals(5, window.length);
        }
        for (int[] window : new GameBoard(10, 10, 6).windows()) {
            assertEquals(6, window.length);
        }
    }

    @Test
    public void correctWindowContents() {
        GameBoard board = new GameBoard(6, 7, 4);
        // Bounds test: all indices in windows() should be valid indices into the board array.
        for (int[] window : board.windows()) {
            for (int idx : window) {
                assertTrue(idx >= 0 && idx < board.getNumRows() * board.getNumColumns(), "Window index out of bounds: " + idx);
            }
        }
        for (int[] window : new GameBoard(8, 8, 5).windows()) {
            for (int idx : window) {
                assertTrue(idx >= 0 && idx < 8 * 8, "Window index out of bounds: " + idx);
            }
        }
        for (int[] window : new GameBoard(10, 10, 6).windows()) {
            for (int idx : window) {
                assertTrue(idx >= 0 && idx < 10 * 10, "Window index out of bounds: " + idx);
            }
        }

        // Additional test: check that the windows are actually in the correct order (i.e., they are contiguous in the board array).
        for (int[] window : new GameBoard(6, 7, 4).windows()) {
            int firstRow = window[0] / board.getNumColumns();
            int firstCol = window[0] % board.getNumColumns();
            int secondRow = window[1] / board.getNumColumns();
            int secondCol = window[1] % board.getNumColumns();
            int rowStep = secondRow - firstRow;
            int colStep = secondCol - firstCol;

            assertTrue(
                (rowStep == 0 && colStep == 1) ||
                (rowStep == 1 && colStep == 0) ||
                (rowStep == 1 && Math.abs(colStep) == 1),
                "Window must be horizontal, vertical, or diagonal"
            );

            for (int i = 1; i < window.length; i++) {
                int currRow = window[i] / board.getNumColumns();
                int currCol = window[i] % board.getNumColumns();
                assertTrue(
                    currRow == firstRow + i * rowStep &&
                    currCol == firstCol + i * colStep,
                    "All positions in a window must follow the same direction"
                );
            }
        }

        for (int[] window : new GameBoard(8, 8, 5).windows()) {
            int firstRow = window[0] / 8;
            int firstCol = window[0] % 8;
            int secondRow = window[1] / 8;
            int secondCol = window[1] % 8;
            int rowStep = secondRow - firstRow;
            int colStep = secondCol - firstCol;

            assertTrue(
                (rowStep == 0 && colStep == 1) ||
                (rowStep == 1 && colStep == 0) ||
                (rowStep == 1 && Math.abs(colStep) == 1),
                "Window must be horizontal, vertical, or diagonal"
            );

            for (int i = 1; i < window.length; i++) {
                int currRow = window[i] / 8;
                int currCol = window[i] % 8;
                assertTrue(
                    currRow == firstRow + i * rowStep &&
                    currCol == firstCol + i * colStep,
                    "All positions in a window must follow the same direction"
                );
            }
        }

        for (int[] window : new GameBoard(10, 10, 6).windows()) {
            int firstRow = window[0] / 10;
            int firstCol = window[0] % 10;
            int secondRow = window[1] / 10;
            int secondCol = window[1] % 10;
            int rowStep = secondRow - firstRow;
            int colStep = secondCol - firstCol;

            assertTrue(
                (rowStep == 0 && colStep == 1) ||
                (rowStep == 1 && colStep == 0) ||
                (rowStep == 1 && Math.abs(colStep) == 1),
                "Window must be horizontal, vertical, or diagonal"
            );

            for (int i = 1; i < window.length; i++) {
                int currRow = window[i] / 10;
                int currCol = window[i] % 10;
                assertTrue(
                    currRow == firstRow + i * rowStep &&
                    currCol == firstCol + i * colStep,
                    "All positions in a window must follow the same direction"
                );
            }
        }
    }
}