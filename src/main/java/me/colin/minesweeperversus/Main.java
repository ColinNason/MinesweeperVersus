package me.colin.minesweeperversus;
import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import java.io.Serial;
import java.net.MalformedURLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Main extends JFrame{
    @Serial
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    static JFrame jFrame = null;

    static JPanel panel = new JPanel();
    GameBoardPanel board = new GameBoardPanel();
    static Score score = new Score();

    // Constructor to set up all the UI and game components
    public Main() throws MalformedURLException {
        jFrame = this;
        Container cp = this.getContentPane();           // JFrame's content-pane
        cp.setLayout(new BorderLayout()); // in 10x10 GridLayout

        panel.add(new Timer(), BorderLayout.WEST);
        panel.add(score, BorderLayout.CENTER);
        cp.add(panel, BorderLayout.NORTH);
        cp.add(board, BorderLayout.CENTER);

        UIManager.put("Button.select", Color.TRANSLUCENT); // Button Click Colour

        board.newGame();

        pack();  // Pack the UI components, instead of setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        setTitle("Mineswepper");
        setVisible(true);   // show it
    }
    public static void main(String[] args) throws MalformedURLException {
        Main f =new Main();


    }

    public static void clickedMine() {
        //panel.setBackground(Color.RED);
    }
}
