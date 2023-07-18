package me.colin.minesweeperversus;

import java.awt.*;
import java.util.Random;

public class MineMap {
    // package access
    int numMines;
    boolean[][] isMined = new boolean[Constants.ROWS][Constants.COLS];
    public long bitboard;
    // default is false

    // Constructor
    public MineMap() {
        super();
    }

    // Allow user to change the rows and cols
    public void newMineMap(int numMines) {
        this.numMines = numMines;


        for(int i = 0; i < numMines;) {
            int x = new Random().nextInt(Constants.ROWS);
            int y = new Random().nextInt(Constants.COLS);

            if(!isMined[x][y]) {

                // Set the corresponding bit in the bitboard
                bitboard |= (1L << position(x,y));

                isMined[x][y] = true;
                i++;
            }
        }

        Main.bitboard.setForeground(Color.WHITE);
        Main.bitboard.setText(bitboard+"");

    }

    public static int position(int x, int y) {
        return x * Constants.ROWS + (y);
    }

    public void interpretBitboard(long bitboard) {
        Main.bitboard.setForeground(Color.WHITE);
        Main.bitboard.setText(bitboard+"");
        for (int x = 0; x < Constants.ROWS; x++) {
            for (int y = 0; y < Constants.COLS; y++) {

                boolean hasMine = ((bitboard) & 1L) == 1L;
                bitboard = bitboard >> 1L;

                isMined[x][y] = hasMine;
            }
        }
    }
}