package me.colin.minesweeperversus;
import java.awt.*;
import java.io.Serial;
import java.net.MalformedURLException;
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
        new Main();


    }

    static Thread thread;

    public static void clickedMine() {
        Color oldColour = Color.RED;
        panel.setBackground(oldColour);
        int FadeSteps = 75;
        Color colour = Color.white;
        final int dRed = colour.getRed() - oldColour.getRed();
        final int dGreen = colour.getGreen() - oldColour.getGreen();
        final int dBlue = colour.getBlue() - oldColour.getBlue();
        if(thread != null) thread.stop(); //TODO end loop/thread safely
        thread = new Thread(() -> { // Make a separate thread to not freeze the main thread
            for (int i = 0; i <= FadeSteps; i++) {
                final Color c = new Color(
                        oldColour.getRed() + ((dRed * i) / FadeSteps),
                        oldColour.getGreen() + ((dGreen * i) / FadeSteps),
                        oldColour.getBlue() + ((dBlue * i) / FadeSteps));
                panel.setBackground(c);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            thread = null;
        });
        thread.start();
    }
}
