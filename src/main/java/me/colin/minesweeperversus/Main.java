package me.colin.minesweeperversus;
import me.colin.minesweeperversus.info.FlagsLeft;
import me.colin.minesweeperversus.info.Score;
import me.colin.minesweeperversus.info.Timer;

import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;

public class Main extends JFrame{
    @Serial
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    static JFrame jFrame = null;

    static JPanel panel = new JPanel();
    GameBoardPanel board;
    static Score score = new Score();
    static FlagsLeft flagsLeft = new FlagsLeft();
    static JLabel bitboard = new JLabel();


    // Constructor to set up all the UI and game components
    public Main(boolean host) throws IOException {
        jFrame = this;
        board = new GameBoardPanel(this, host);
        Container cp = this.getContentPane();           // JFrame's content-pane
        cp.setLayout(new BorderLayout()); // in 10x10 GridLayout

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));

        panel.add(flagsLeft);
        panel.add(new Timer());
        panel.add(score);
        //panel.add(bitboard);
        panel.setBackground(Color.decode("#4A752C"));

        cp.add(panel, BorderLayout.NORTH);
        cp.add(board, BorderLayout.CENTER);

        UIManager.put("Button.select", Color.TRANSLUCENT); // Button Click Colour

        board.newGame();

        pack();  // Pack the UI components, instead of setSize()
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        setTitle("Minesweeper");
        setVisible(true);   // show it
        setIconImage(new ImageIcon(new URL("https://www.google.com/logos/fnbx/minesweeper/flag_icon.png")).getImage());
    }
    public static void main(String[] args) throws IOException {
        new Main(true);
        new Main(false);

    }
}
