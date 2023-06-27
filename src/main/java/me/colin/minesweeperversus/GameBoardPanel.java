package me.colin.minesweeperversus;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.net.MalformedURLException;
import javax.swing.*;

import static me.colin.minesweeperversus.Constants.COLS;
import static me.colin.minesweeperversus.Constants.ROWS;


public class GameBoardPanel extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;  // Cell width and height, in pixels
    public static final int CANVAS_WIDTH  = CELL_SIZE * COLS; // Game board width/height
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;

    // Define properties (package-visible)
    /** The game board composes of ROWSxCOLS cells */
    static Cell[][] cells = new Cell[ROWS][COLS];
    /** Number of mines */
    int numMines = 10;
    static int numTotal = ROWS * COLS;

    static boolean doRender = true;

    static boolean freshStart = true;

    static JPanel board;

    /** Constructor */
    public GameBoardPanel() throws MalformedURLException {
        super.setLayout(new GridLayout(ROWS, COLS, 0, 0));  // JPanel
        board = this;

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
        isRevealed = new boolean[ROWS][COLS];
        // Get a new mine map

        MineMap mineMap = new MineMap();
        mineMap.newMineMap(numMines);

        this.setVisible(false);
        // Reset cells, mines, and flags
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                // Initialize each cell with/without mine
                cells[row][col].newGame(mineMap.isMined[row][col]);
                surroundingMineCounts[row][col] = -1;
            }
        }
        this.setVisible(true);
    }

    public static boolean[][] isRevealed = new boolean[ROWS][COLS];
    private static int[][] surroundingMineCounts = new int[ROWS][COLS];

    // Return the number of mines [0, 8] in the 8 neighboring cells
    //  of the given cell at (srcRow, srcCol).
    public static int getSurroundingMines(int srcRow, int srcCol) {
//        int numMines = 0;
//        for (int row = srcRow - 1; row <= srcRow + 1; row++) {
//            for (int col = srcCol - 1; col <= srcCol + 1; col++) {
//                // Need to ensure valid row and column numbers too
//                if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
//                    if (cells[row][col].isMine) numMines++;
//                }
//            }
//        }
//        return numMines;
        if (surroundingMineCounts[srcRow][srcCol] != -1) {
            return surroundingMineCounts[srcRow][srcCol];
        }

        int numMines = 0;
        for (int row = Math.max(0, srcRow - 1); row <= Math.min(srcRow + 1, ROWS - 1); row++) {
            for (int col = Math.max(0, srcCol - 1); col <= Math.min(srcCol + 1, COLS - 1); col++) {
                if (row != srcRow || col != srcCol) {
                    if (cells[row][col].isMine) {
                        numMines++;
                    }
                }
            }
        }

        surroundingMineCounts[srcRow][srcCol] = numMines;
        return numMines;
    }

    // Reveal the cell at (srcRow, srcCol)
    // If this cell has 0 mines, reveal the 8 neighboring cells recursively
    private void revealCell(int srcRow, int srcCol) {
//        int numMines = getSurroundingMines(srcRow, srcCol);
//        cells[srcRow][srcCol].setText(numMines + "");
//        cells[srcRow][srcCol].isRevealed = true;
//        cells[srcRow][srcCol].paint();  // based on isRevealed
//        numTotal--;
//        if (numMines == 0) {
//            // Recursively reveal the 8 neighboring cells
//            for (int row = srcRow - 1; row <= srcRow + 1; row++) {
//                for (int col = srcCol - 1; col <= srcCol + 1; col++) {
//                    // Need to ensure valid row and column numbers too
//                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
//                        if (!cells[row][col].isRevealed) revealCell(row, col);
//                    }
//                }
//            }
//        }

        if (isRevealed[srcRow][srcCol]) {
            return; // Cell already revealed
        }

        int numMines = getSurroundingMines(srcRow, srcCol);
        cells[srcRow][srcCol].setText(numMines + "");
        isRevealed[srcRow][srcCol] = true;
        cells[srcRow][srcCol].paint();  // based on isRevealed
        numTotal--;

        board.setVisible(false);

        if (numMines == 0) {
            for (int row = Math.max(0, srcRow - 1); row <= Math.min(srcRow + 1, ROWS - 1); row++) {
                for (int col = Math.max(0, srcCol - 1); col <= Math.min(srcCol + 1, COLS - 1); col++) {
                    if (row != srcRow || col != srcCol) {
                        revealCell(row, col);
                    }
                }
            }
        }
        board.setVisible(true);
    }

    private class CellMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {         // Get the source object that fired the Event
            Cell sourceCell = (Cell)e.getSource();
            System.out.println("Click");

            if(isRevealed[sourceCell.row][sourceCell.col]) return;

            // Left-click to reveal a cell; Right-click to plant/remove the flag.
            if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
                if(freshStart) {
                    if(sourceCell.isMine || getSurroundingMines(sourceCell.row,sourceCell.col) > 0) {
                        doRender = false;
                        newGame();
                        mousePressed(e);
                        return;
                    }
                    doRender = true;
                    freshStart = false;
                }

                if(sourceCell.isFlagged) return;

                if(sourceCell.isMine) {
                    Main.clickedMine();
                    newGame();
                    mousePressed(e);
                } else {
                    revealCell(sourceCell.row, sourceCell.col);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
                sourceCell.isFlagged = !sourceCell.isFlagged;
                sourceCell.paint();
            }

            if(numTotal == numMines) {
                newGame();
                Main.score.win();
                mousePressed(e);
            }
        }
    }
}