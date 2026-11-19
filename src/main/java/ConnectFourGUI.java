import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * Displays a Connect Four board and forwards player clicks to GameEngine.
 * The engine owns the tokens and turns; this class draws that state and handles
 * window controls.
 */
public class ConnectFourGUI extends Application {
    //private final GameEngine engine = new GameEngine();
    private GameEngine engine;
    private Circle[][] cells;
    private final Map<Integer, String> tokenColors = new HashMap<>();
    // Keep references to the shapes so moves can update the existing
    // view without rebuilding the entire window. Cell indices match GameBoard.
    //private final Circle[][] cells = new Circle[engine.getBoard().getNumRows()][engine.getBoard().getNumColumns()];
    private final Circle hoverToken = new Circle(27);
    private final StackPane hoverSlot = new StackPane(hoverToken);
    private final GridPane grid = new GridPane();
    private int hoveredColumn = -1;
    // Status shows the next player; feedback explains the most recent action.
    private final Label status = new Label();
    private final Label feedback = new Label("Click a column to drop a token.");

    public ConnectFourGUI() {
        tokenColors.put(Token.RED, "#dc143c");
        tokenColors.put(Token.BLACK, "#222222");
        tokenColors.put(null, "#ffffff");
    }

    @Override
    public void init() {

        List<String> args = getParameters().getUnnamed();
        // Default dimension when no arguments are supplied
        int rows = 6;
        int columns = 7;

        if(!args.isEmpty()) {
            if(args.size() != 2) {
                throw new IllegalArgumentException(
                    "Expected two arguments: rows & columns."
                );
            }
            rows = Integer.parseInt(args.get(0));
            columns = Integer.parseInt(args.get(1));

            if(rows < 4 || columns < 4) {
                throw new IllegalArgumentException(
                    "Rows and columns much each be at least 4. It is Connect 4, after all."
                );
            }

        }
        engine = new GameEngine(rows, columns);
        cells = new Circle[rows][columns];
    }

    /** JavaFX calls this once to build and show the application's main window. */
    @Override
    public void start(Stage stage) {
        // Start over with an empty board and red's turn.
        Button reset = new Button("New board");
        reset.setOnAction(event -> {
            engine.reset();
            feedback.setText("Click a column to drop a token.");
            refreshBoard();
        });
        HBox toolbar = new HBox(12, reset, status);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        // Row 0 reserves space for a token preview; rows 1–x are board cells.
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(6);
        grid.setVgap(6);
        grid.setPadding(new Insets(12));
        // Only the playable rows have a blue background; the preview row
        // uses the surrounding window's background.
        Region boardBackground = new Region();
        boardBackground.setStyle("-fx-background-color: #2455a4; -fx-background-radius: 12;");
        boardBackground.setMouseTransparent(true);
        GridPane.setMargin(boardBackground, new Insets(-3, -12, -12, -12));
        grid.add(boardBackground, 0, 1, engine.getBoard().getNumColumns(), engine.getBoard().getNumRows());
        hoverSlot.setMinSize(64, 64);
        hoverSlot.setMouseTransparent(true);
        hoverToken.setStroke(Color.web("#17386d"));
        hoverToken.setVisible(false);
        grid.add(hoverSlot, 0, 0);
        for (int column = 0; column < engine.getBoard().getNumColumns(); column++) {
            for (int row = 0; row < engine.getBoard().getNumRows(); row++) {
                Circle cell = new Circle(27);
                cell.setStroke(Color.web("#17386d"));
                cells[row][column] = cell;
                // The slot provides a larger click target around the circle.
                // Clicking any row requests a drop into that column; the engine
                // chooses the lowest empty cell rather than the clicked cell.
                StackPane slot = new StackPane(cell);
                slot.setMinSize(64, 64);
                grid.add(slot, column, row + 1);
            }
        }
        grid.setOnMouseMoved(event -> updateHover(event.getSceneX(), event.getSceneY()));
        grid.setOnMouseEntered(event -> updateHover(event.getSceneX(), event.getSceneY()));
        grid.setOnMouseExited(event -> {
            hoveredColumn = -1;
            hoverToken.setVisible(false);
        });
        grid.setOnMouseClicked(event -> {
            updateHover(event.getSceneX(), event.getSceneY());
            if (event.getButton() == MouseButton.PRIMARY && hoveredColumn >= 0) {
                placeToken(hoveredColumn);
            }
        });

        // Stack the toolbar and feedback above the board in the main layout.
        VBox top = new VBox(10, toolbar, feedback);
        top.setPadding(new Insets(0, 0, 14, 0));
        BorderPane root = new BorderPane(grid);
        root.setTop(top);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #eef2f7; -fx-font-size: 14px;");
        // Apply the current model's colors and turn label before showing the stage.
        refreshBoard();
        stage.setTitle("Connect Four");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    /** Requests a move, reports whether it succeeded, and redraws the current state. */
    private void placeToken(int column) {
        if (engine.placeToken(column)) {
            switch (engine.gameState()) {
                case 1:
                    feedback.setText("Red Won!");
                    break;
                case -1:
                    feedback.setText("Black won!");
                    break;
                case 0:
                    feedback.setText("Draw");
                    break;
                case null:
                    feedback.setText("Token placed in column " + (column + 1) + ".");
                    break;
                default:
                    break;
            }
        } else {
            feedback.setText("Column " + (column + 1) + " is full or game is over.");
        }
        refreshBoard();
    }

    /**
     * Finds the column under the mouse and positions the preview token above it.
     * Hides the preview when the mouse is outside the preview and board cells.
     * @param sceneX horizontal mouse position relative to the scene
     * @param sceneY vertical mouse position relative to the scene
     */
    private void updateHover(double sceneX, double sceneY) {
        // Cell bounds use grid coordinates, so convert the scene position first.
        Point2D mouse = grid.sceneToLocal(sceneX, sceneY);
        // -1 means no column is selected unless the checks below find one.
        hoveredColumn = -1;

        // Row 0 holds the preview, row GameBoard.ROWS is the bottom playable row.
        // These corner cells define the active area
        Bounds first = grid.getCellBounds(0, 0);
        Bounds last = grid.getCellBounds(engine.getBoard().getNumColumns() - 1, engine.getBoard().getNumRows());
        if (mouse.getX() >= first.getMinX() && mouse.getX() <= last.getMaxX()
                && mouse.getY() >= first.getMinY() && mouse.getY() <= last.getMaxY()) {
            for (int column = 0; column < engine.getBoard().getNumColumns(); column++) {
                Bounds bounds = grid.getCellBounds(column, 0);
                // Assign half of each horizontal gap to the neighboring column
                // so the preview still appears when the mouse is between cells.
                if (mouse.getX() >= bounds.getMinX() - grid.getHgap() / 2
                        && mouse.getX() < bounds.getMaxX() + grid.getHgap() / 2) {
                    hoveredColumn = column;
                    break;
                }
            }
        }
        // Move the preview slot horizontally while keeping it in row 0.
        if (hoveredColumn >= 0) {
            GridPane.setColumnIndex(hoverSlot, hoveredColumn);
        }
        // Hide the token if no valid column was found at this mouse position.
        hoverToken.setVisible(hoveredColumn >= 0);
    }

    /** Synchronizes cell colors, the hover token, and the turn label. */
    private void refreshBoard() {
        // Assume the board is full until at least one empty cell is found.
        boolean full = true;
        for (int column = 0; column < engine.getBoard().getNumColumns(); column++) {
            boolean columnFull = true;
            for (int row = 0; row < engine.getBoard().getNumRows(); row++) {
                Token token = engine.getBoard().getToken(row, column);
                // Empty cells are white, occupied cells use the token's color.
                Integer player = null;
                if (token != null) {
                    player = token.getColor();
                }
                cells[row][column].setFill(Color.web(tokenColors.get(player)));
                if (token == null) columnFull = false;
            }
            if (!columnFull) full = false;
        }
        hoverToken.setFill(Color.web(tokenColors.get(engine.getCurrentPlayer())));
        if (full) {
            status.setText("Board full");
        } else if (engine.getCurrentPlayer() == Token.RED) {
            status.setText("Red's turn");
        } else {
            status.setText("Black's turn");
        }
    }

    /** JavaFX Starter */
    public static void main(String[] args) {
        launch(args);
    }
}
