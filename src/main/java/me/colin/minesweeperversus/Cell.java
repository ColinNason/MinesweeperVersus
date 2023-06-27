package me.colin.minesweeperversus;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.Serial;
import java.net.MalformedURLException;
import java.net.URL;

import static me.colin.minesweeperversus.GameBoardPanel.getSurroundingMines;
import static me.colin.minesweeperversus.GameBoardPanel.isRevealed;

public class Cell extends JButton {
    @Serial
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for JButton's colors and fonts
    //  to be chosen based on cell's state
    public static final Color[] BG_NOT_REVEALED = new Color[]{Color.decode("#AAD751"), Color.decode("#A2D149"), Color.decode("#BFE17D"), Color.decode("#B9DD77")};
    public static final Color FG_NOT_REVEALED = Color.RED;    // flag, mines
    public static final Color[] BG_REVEALED = new Color[]{Color.decode("#E5C29F"), Color.decode("#D7B899"), Color.decode("#ECD1B7"), Color.decode("#E1CAB3")};
    public static final Color[] FG_REVEALED = new Color[]{new Color(0f,0f,0f,0f), Color.decode("#1976D2"), Color.decode("#388E3C"), Color.decode("#D32F2F"), Color.decode("#7B1FA2"), Color.decode("#FF8F00"), Color.ORANGE, Color.CYAN, Color.BLACK}; // number of mines
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

    // Define properties (package-visible)
    /**
     * The row and column number of the cell
     */
    int row, col;
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
    public Cell(int row, int col) throws MalformedURLException {
        super();   // JTextField
        this.row = row;
        this.col = col;
        // Set JButton's default display properties
        super.setFont(FONT_NUMBERS);
        super.setBorderPainted(false);
        super.setMultiClickThreshhold(0);

        JButton button = this;
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if(GameBoardPanel.getSurroundingMines(row,col) == 0 && isRevealed[row][col]) return;
                button.setBackground(getColor(true));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(getColor(false));
            }
        });
    }

    /**
     * Reset this cell, ready for a new game
     */
    public void newGame(boolean isMine) {
        this.isFlagged = false;  // default
        this.isMine = isMine;
        super.setEnabled(true);  // enable button
        super.setText("");       // display blank
        paint();
    }

    ImageIcon bigImg = new ImageIcon(new URL("https://www.google.com/logos/fnbx/minesweeper/flag_icon.png")); // src/main/resources/
    Image image = bigImg.getImage(); // transform it
    Image icon = image.getScaledInstance(50, 50,  Image.SCALE_SMOOTH); // scale it the smooth way

    /**
     * Paint itself based on its status
     */
    public void paint() {
        int numMines = getSurroundingMines(this.row, this.col);
        super.setForeground(isRevealed[this.row][this.col] ? FG_REVEALED[numMines] : FG_NOT_REVEALED);
        Color color = getColor(false);

        super.setIcon(isFlagged ? new ImageIcon(icon) : null);

        super.setBackground(color);
    }


    public Color getColor(boolean hover) {
        int i = 0;
        if (hover) i = 2;
        Color color = BG_NOT_REVEALED[i];
        if (this.row % 2 == this.col % 2)
            color = BG_NOT_REVEALED[1 + i];
        if (isRevealed[this.row][this.col]) {
            color = BG_REVEALED[i];
            if (this.row % 2 == this.col % 2)
                color = BG_REVEALED[1 + i];
        }
        //if (isFlagged) color = FG_NOT_REVEALED;
        return color;
    }

}
