
/**
 * Represents a Connect Four piece, using 'r' for red and 'b' for black.
 * Empty board cells are represented by null, without a Token object.
 */
public class Token {

    public static final char RED = 'r';
    public static final char BLACK = 'b';

    private char color;

    /**
     * Creates a token with the given color
     * @param color the color code, expected to be r xor b
     */
    public Token(char color) {
        setColor(color);
    }

    /** Returns the stored color code used by the board's display map. */
    public char getColor() {
        return color;
    }

    /**
     * Changes the color of this piece.
     * @param color the lowercase color code: 'r' or 'b'; stored as supplied
     */
    public void setColor(char color) {
        this.color = color;
    }

}
