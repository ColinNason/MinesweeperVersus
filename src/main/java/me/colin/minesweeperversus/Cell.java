package me.colin.minesweeperversus;

import me.colin.minesweeperversus.fonts.Roboto;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static me.colin.minesweeperversus.Constants.COLS;
import static me.colin.minesweeperversus.Constants.ROWS;
import static me.colin.minesweeperversus.GameBoardPanel.*;

public class Cell extends JButton {
    @Serial
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for JButton's colors and fonts
    //  to be chosen based on cell's state
    public static final Color[] BG_NOT_REVEALED = new Color[]{Color.decode("#AAD751"), Color.decode("#A2D149"), Color.decode("#BFE17D"), Color.decode("#B9DD77")};
    public static final Color FG_NOT_REVEALED = Color.RED;    // flag, mines
    public static final Color[] BG_REVEALED = new Color[]{Color.decode("#E5C29F"), Color.decode("#D7B899"), Color.decode("#ECD1B7"), Color.decode("#E1CAB3")};
    public static final Color[] FG_REVEALED = new Color[]{new Color(0f,0f,0f,0f), Color.decode("#1976D2"), Color.decode("#388E3C"), Color.decode("#D32F2F"), Color.decode("#7B1FA2"), Color.decode("#FF8F00"), Color.ORANGE, Color.CYAN, Color.BLACK}; // number of mines
    public static final Font FONT_NUMBERS = Roboto.BOLD.deriveFont(30f); // small 20, Monospaced

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
    int isFlagged;

    /**
     * Constructor
     */
    public Cell(int row, int col) throws IOException {
        super();   // JTextField
        this.row = row;
        this.col = col;
        // Set JButton's default display properties
        super.setFont(FONT_NUMBERS);
        //super.setBorderPainted(true);
        super.setDoubleBuffered(true);
        super.setFocusable(false); // Highlight text on click false

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
        this.isFlagged = -1;  // default
        this.isMine = isMine;
        super.setEnabled(true);  // enable button
        super.setText("");       // display blank
        paint();
    }

    ImageIcon bigImg = new ImageIcon(new URL("https://www.google.com/logos/fnbx/minesweeper/flag_icon.png"));
    Image image = bigImg.getImage(); // transform it
    Image icon = image.getScaledInstance(50, 50,  Image.SCALE_SMOOTH); // scale it the smooth way


    public static void updateCells() {
        for(int col = 0; col < COLS; col++) {
            for(int row = 0; row < ROWS; row++) {
                if(openCells.contains(cells[row][col])) continue;
                cells[row][col].paint();
            }
        }

        //toUpdate.forEach(Cell::paint);
        //toUpdate = new HashSet<>();
    }

    /**
     * Paint itself based on its status
     */
    public void paint() {
        if(!doRender) return; // If searching for a 0 click on start DONT RENDER
        int numMines = getSurroundingMines(this.row, this.col);
        super.setForeground(isRevealed[this.row][this.col] ? FG_REVEALED[numMines] : FG_NOT_REVEALED);
        Color color = getColor(false);

        // <Border>
        if(!isRevealed[row][col]) {
            MatteBorder border = new MatteBorder(
                    this.row == 0 ? 0 : // if col == 0  return no border
                            isRevealed[-1 + this.row][this.col] ? 3 : 0, // TOP
                    this.col == 0 ? 0 : // if col == 0  return no border
                            isRevealed[this.row][-1 + this.col] ? 3 : 0, // LEFT
                    this.row == -1 + Constants.ROWS ? 0 : // if row == max rows return no border
                            isRevealed[1 + this.row][this.col] ? 3 : 0,// BOTTOM
                    this.col == -1 + Constants.COLS ? 0 : // if col == 0  return no border
                            isRevealed[this.row][1 + this.col] ? 3 : 0, // RIGHT
                    Color.decode("#87AF3A")
            );
            super.setBorder(border);
            super.setBorderPainted(true);
        } else {
            super.setBorderPainted(false);
        }
        // </Border>

        super.setBackground(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isFlagged != -1) {
            new ImageIcon(icons.get(isFlagged)).paintIcon(this, g, (getWidth() - 50) / 2, (getHeight() - 50) / 2);
        }
//        g.setColor(Color.RED);
//        g.fillRect(50, 0, size, size);
    }

    BufferedImage img = ImageIO.read(new URL("https://www.google.com/logos/fnbx/minesweeper/flag_plant.png"));

    ArrayList<Image> icons = imgSplit();

    private ArrayList<Image> imgSplit() {
        int frameWidth = 80;  // Width of each frame
        int frameHeight = 81; // Height of each frame

        ArrayList<Image> frames = new ArrayList<>();

        for (int y = 0; y < img.getHeight(); y += frameHeight) {
            for (int x = 0; x < img.getWidth(); x += frameWidth) {
                BufferedImage frame = img.getSubimage(x, y, frameWidth, frameHeight);
                Image img = frame.getScaledInstance(50, 50,  Image.SCALE_SMOOTH);
                frames.add(img);
            }
        }
        frames.add(icon);
        return frames;
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
        return color;
    }


}
