package me.colin.minesweeperversus;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import static me.colin.minesweeperversus.Board.COLS;
import static me.colin.minesweeperversus.Board.ROWS;


public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;  // Cell width and height, in pixels
    public static final int CANVAS_WIDTH  = CELL_SIZE * COLS; // Game board width/height
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;

    // Define properties (package-visible)
    /** The game board composes of ROWSxCOLS cells */
    static Cell cells[][] = new Cell[ROWS][COLS];
    /** Number of mines */
    int numMines = 10;
    static int numTotal = ROWS * COLS;

    static boolean freshStart = true;

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(ROWS, COLS, -1, -1));  // JPanel

        // Allocate the 2D array of Cell, and added into content-pane.
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        CellMouseListener listener = new CellMouseListener();
        //  Cells (JButtons)
        // .........

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col].addMouseListener(listener);   // For all rows and cols
            }
        }

        // Set the size of the content-pane and pack all the components
        //  under this container.
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    // Initialize and re-initialize a new game
    public void newGame() {
        freshStart = true;
        numTotal = ROWS * COLS;
        // Get a new mine map
        MineMap mineMap = new MineMap();
        mineMap.newMineMap(numMines);

        // Reset cells, mines, and flags
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                // Initialize each cell with/without mine
                cells[row][col].newGame(mineMap.isMined[row][col]);
            }
        }
    }

    // Return the number of mines [0, 8] in the 8 neighboring cells
    //  of the given cell at (srcRow, srcCol).
    public static int getSurroundingMines(int srcRow, int srcCol) {
        int numMines = 0;
        for (int row = srcRow - 1; row <= srcRow + 1; row++) {
            for (int col = srcCol - 1; col <= srcCol + 1; col++) {
                // Need to ensure valid row and column numbers too
                if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                    if (cells[row][col].isMine) numMines++;
                }
            }
        }
        return numMines;
    }

    // Reveal the cell at (srcRow, srcCol)
    // If this cell has 0 mines, reveal the 8 neighboring cells recursively
    private void revealCell(int srcRow, int srcCol) {
        int numMines = getSurroundingMines(srcRow, srcCol);
        cells[srcRow][srcCol].setText(numMines + "");
        cells[srcRow][srcCol].isRevealed = true;
        cells[srcRow][srcCol].paint();  // based on isRevealed
        numTotal--;
        if (numMines == 0) {
            // Recursively reveal the 8 neighboring cells
            for (int row = srcRow - 1; row <= srcRow + 1; row++) {
                for (int col = srcCol - 1; col <= srcCol + 1; col++) {
                    // Need to ensure valid row and column numbers too
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                        if (!cells[row][col].isRevealed) revealCell(row, col);
                    }
                }
            }
        }
    }

    // Return true if the player has won (all cells have been revealed or were mined)
    public boolean hasWon() {
        // ......
        return true;
    }

    // [TODO 2] Define a Listener Inner Class
    private class CellMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {         // Get the source object that fired the Event
            Cell sourceCell = (Cell)e.getSource();
            // For debugging
            System.out.println("You clicked on (" + sourceCell.row + "," + sourceCell.col + ")");

            if(sourceCell.isRevealed) return;

            // Left-click to reveal a cell; Right-click to plant/remove the flag.
            if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
                // [TODO 5] (later, after TODO 3 and 4
                if(freshStart) {
                    if(getSurroundingMines(sourceCell.row,sourceCell.col) > 0) {
                        newGame();
                        mouseClicked(e);
                        return;
                    }
                    freshStart = false;
                }

                if(sourceCell.isFlagged) return;

                if(sourceCell.isMine) {
                    newGame();
                } else {
                    revealCell(sourceCell.row, sourceCell.col);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
                // [TODO 6]
                sourceCell.isFlagged = !sourceCell.isFlagged;
                sourceCell.paint();
            }

            if(numTotal == numMines) {
                newGame();
                Main.score.win();

            }
        }
    }
}