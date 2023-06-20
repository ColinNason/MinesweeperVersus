package me.colin.minesweeperversus;
import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame{
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");

    // Constructor to set up all the UI and game components
    public Main() {
        Container cp = this.getContentPane();           // JFrame's content-pane
        cp.setLayout(new BorderLayout()); // in 10x10 GridLayout

        cp.add(board, BorderLayout.CENTER);

        // Add btnNewGame to the south to re-start the game
        // ......

        board.newGame();

        pack();  // Pack the UI components, instead of setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
        setTitle("Mineswepper");
        setVisible(true);   // show it
    }
    public static void main(String[] args) {
        Main f =new Main();




//        f.setSize(400,500);//400 width and 500 height
//        f.setLayout(null);//using no layout managers
//        f.setVisible(true);//making the frame visible




    }
}
