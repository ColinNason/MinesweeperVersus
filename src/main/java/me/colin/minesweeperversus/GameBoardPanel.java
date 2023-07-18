package me.colin.minesweeperversus;
import me.colin.minesweeperversus.info.FlagsLeft;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.Serial;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.*;

import static me.colin.minesweeperversus.CellAnimation.*;
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
    public static int numMines = 10;
    static int numTotal = ROWS * COLS;

    static boolean doRender = true;

    static boolean freshStart = true;

    static JPanel board;

    public boolean host;

    public Main instance;

    /** Constructor */
    public GameBoardPanel(Main mainInstance, boolean host) throws IOException {
        super.setLayout(new GridLayout(ROWS, COLS, 0, 0));  // JPanel
        board = this;
        this.host = host;
        instance = mainInstance;

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
        if(host) {
            MineMap mineMap = new MineMap();
            mineMap.newMineMap(numMines);
            newGameInner(mineMap);
        } else {

        }
    }

    public void newGame(long bitboard) {
        MineMap mineMap = new MineMap();
        mineMap.interpretBitboard(bitboard);
        newGameInner(mineMap);


    }

    private void newGameInner(MineMap mineMap) {
        freshStart = true;
        numTotal = ROWS * COLS;
        isRevealed = new boolean[ROWS][COLS];
        // Get a new mine map

        //mineMap.newMineMap(numMines);
        //mineMap.interpretBitboard(0b11111111111);

        FlagsLeft.left = numMines;
        FlagsLeft.update();

        openCells = new HashSet<>();

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
    private static final int[][] surroundingMineCounts = new int[ROWS][COLS];

    // Return the number of mines [0, 8] in the 8 neighboring cells
    //  of the given cell at (srcRow, srcCol).
    public static int getSurroundingMines(int srcRow, int srcCol) {
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

    public static HashSet<Cell> openCells = new HashSet<>();

    private void revealCell(int srcRow, int srcCol, boolean topLevelRecursion){
        if (isRevealed[srcRow][srcCol]) {
            return; // Cell already revealed
        }

        try {
            new CellAnimation(cells[srcRow][srcCol]);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int numMines = getSurroundingMines(srcRow, srcCol);
        cells[srcRow][srcCol].setText(numMines + "");
        isRevealed[srcRow][srcCol] = true;
        cells[srcRow][srcCol].isFlagged = -1;
        cells[srcRow][srcCol].paint();
        openCells.add(cells[srcRow][srcCol]);  // based on isRevealed
        numTotal--;


        board.setVisible(false);

        if (numMines == 0) {
            for (int row = Math.max(0, srcRow - 1); row <= Math.min(srcRow + 1, ROWS - 1); row++) {
                for (int col = Math.max(0, srcCol - 1); col <= Math.min(srcCol + 1, COLS - 1); col++) {
                    if (row != srcRow || col != srcCol) {
                        revealCell(row, col, false);
                    }
                }
            }
        }
        if(topLevelRecursion) {
            Cell.updateCells(); // Batch updates (Cell borders)
        }
        board.setVisible(true);
    }

    private class CellMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {         // Get the source object that fired the Event
            Cell sourceCell = (Cell)e.getSource();

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

                // <Cording>
                if(isRevealed[sourceCell.row][sourceCell.col]) {


                    int srcRow = sourceCell.row;
                    int srcCol = sourceCell.col;
                    int numFlags = 0;
                    ArrayList<Cell> cellList = new ArrayList<>();
                    for (int row = Math.max(0, srcRow - 1); row <= Math.min(srcRow + 1, ROWS - 1); row++) {
                        for (int col = Math.max(0, srcCol - 1); col <= Math.min(srcCol + 1, COLS - 1); col++) {
                            if (row != srcRow || col != srcCol) {
                                if (cells[row][col].isFlagged != -1) {
                                    numFlags++;
                                } else
                                if (!isRevealed[row][col]) {
                                    cellList.add(cells[row][col]);
                                }
                            }
                        }
                    }

                    if(getSurroundingMines(srcRow, srcCol) == numFlags) {
                        for (Cell cell : cellList) {
                            e.setSource(cell);
                            mousePressed(e);
                        }
                    }
                    return;
                }
                // </Cording> //TODO Move this function to Right and Left click


                if(sourceCell.isFlagged != -1) return;

                if(sourceCell.isMine) {
                    new ClickedMine();
                    newGame();
                    mousePressed(e);
                } else {
                    revealCell(sourceCell.row, sourceCell.col, true);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
                if(isRevealed[sourceCell.row][sourceCell.col]) return;
                //sourceCell.isFlagged = !sourceCell.isFlagged;



                if(sourceCell.isFlagged != -1) {
                    FlagsLeft.left++;
                    sourceCell.isFlagged = -1;
                } else {
                    FlagsLeft.left--;
                    sourceCell.isFlagged = 0;
                    flagAnimation(sourceCell);
                }
                FlagsLeft.update();
                sourceCell.paint();
            }

            if(numTotal == numMines) {
                newGame();
                instance.score.win();
                mousePressed(e);
            }
        }

        //TODO Left and right  click


    }


    private void flagAnimation(Cell sourceCell) {
        Thread thread = new Thread(() -> { // Make a separate thread to not freeze the main thread
            while(sourceCell.isFlagged != -1 && sourceCell.isFlagged < 10) {
                sourceCell.isFlagged++;
                sourceCell.paint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    static ArrayList<CellAnimation> animationList = new ArrayList<>();

    public static void update() {
        board.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        ArrayList<CellAnimation> toRemove = new ArrayList<>();
        animationList.forEach(cellAnimation -> { //TODO Modification mid execution can cause runtime errors
            //if(cellAnimation == null) return;

            Graphics2D g2d = (Graphics2D)g;
            g2d.setColor(cellAnimation.color);
            Rectangle rect2 = new Rectangle(0, 0, cellAnimation.size, cellAnimation.size);

            AffineTransform tx = new AffineTransform();
            tx.rotate(Math.toRadians(cellAnimation.currentRotation));

            AffineTransform t = new AffineTransform();
            t.translate(cellAnimation.posX,cellAnimation.posY);
            t.concatenate(tx);

            Shape newShape = t.createTransformedShape(rect2);
            g2d.draw(newShape);
            g2d.fill(newShape);

            if(cellAnimation.toRemove) toRemove.add(cellAnimation);
        });
        animationList.removeAll(toRemove);
    }
}