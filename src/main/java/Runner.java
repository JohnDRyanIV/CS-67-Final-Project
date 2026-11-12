
public class Runner {
    public static void main(String[] args) {
        GameBoard board = new GameBoard();
        // currently just demonstrating board display on console
        // first row empty for visual output purposes
        int colorPicker = 0;
        char currentColor;
        for(int row = 1; row < GameBoard.ROWS; row++) {
            for(int col = 0; col < GameBoard.COLUMNS; col++) {
                if(colorPicker % 2 == 0) { // red
                    currentColor = 'r';
                }
                else { // black
                    currentColor = 'b';
                }
                colorPicker++;
                board.board[row][col] = new Token(currentColor);
            }
        }
        board.display();
    }
}
