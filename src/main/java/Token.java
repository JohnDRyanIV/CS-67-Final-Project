
/**
 * Represents a Connect Four piece, using 1 for red and -1 for black.
 * Empty board cells are represented by null, without a Token object.
 */
public class Token {

    public static final int RED = 1;
    public static final int BLACK = -1;

    private int color;

    /**
     * Creates a token with the given color
     * @param color the color code, expected to be RED (1) or BLACK (-1)
     */
    public Token(int color) {
        setColor(color);
    }

    /** Returns the stored color code used by the board's display map. */
    public int getColor() {
        return color;
    }

    /**
     * Changes the color of this piece.
     * @param color the color code: 1 for red or -1 for black; stored as supplied
     */
    public void setColor(int color) {
        this.color = color;
    }

    /* Another endpoint for getting the color - could be more intuitive
     * to call when evaluating game state for AI */
    public int getTokenValue() {
        return getColor();
    }

}
