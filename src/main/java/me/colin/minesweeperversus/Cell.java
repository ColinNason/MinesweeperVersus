package me.colin.minesweeperversus;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

import static me.colin.minesweeperversus.GameBoardPanel.getSurroundingMines;

public class Cell extends JButton {
    @Serial
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for JButton's colors and fonts
    //  to be chosen based on cell's state
    public static final Color[] BG_NOT_REVEALED = new Color[]{Color.decode("#A7D948"), Color.decode("#8ECC39")};
    public static final Color FG_NOT_REVEALED = Color.RED;    // flag, mines
    public static final Color[] BG_REVEALED = new Color[]{Color.decode("#E5C29F"), Color.decode("#D7B899")};
    public static final Color[] FG_REVEALED = new Color[]{new Color(0f,0f,0f,0f), Color.BLUE, Color.GREEN, Color.RED, Color.PINK, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.BLACK}; // number of mines
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

    // Define properties (package-visible)
    /**
     * The row and column number of the cell
     */
    int row, col;
    /**
     * Already revealed?
     */
    boolean isRevealed;
    /**
     * Is a mine?
     */
    boolean isMine;
    /**
     * Is Flagged by player?
     */
    boolean isFlagged;

    /**
     * Constructor
     */
    public Cell(int row, int col) {
        super();   // JTextField
        this.row = row;
        this.col = col;
        // Set JButton's default display properties
        super.setFont(FONT_NUMBERS);
    }

    /**
     * Reset this cell, ready for a new game
     */
    public void newGame(boolean isMined) {
        this.isRevealed = false; // default
        this.isFlagged = false;  // default
        this.isMine = isMined;  // given
        super.setEnabled(true);  // enable button
        super.setText("");       // display blank
        paint();
    }

    /**
     * Paint itself based on its status
     */
    public void paint() {
        int numMines = getSurroundingMines(this.row, this.col);
        super.setForeground(isRevealed ? FG_REVEALED[numMines] : FG_NOT_REVEALED);
        Color color = getColor();
        super.setBackground(color);
    }


    public Color getColor() {
        Color color = BG_NOT_REVEALED[0];
        if (this.row % 2 == this.col % 2)
            color = BG_NOT_REVEALED[1];
        if (isRevealed) {
            color = BG_REVEALED[0];
            if (this.row % 2 == this.col % 2)
                color = BG_REVEALED[1];
        }
        if (isFlagged) color = FG_NOT_REVEALED;
        return color;
    }
}
