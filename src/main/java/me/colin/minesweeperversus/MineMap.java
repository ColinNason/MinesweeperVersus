package me.colin.minesweeperversus;

import java.util.Random;

public class MineMap {
    // package access
    int numMines;
    boolean[][] isMined = new boolean[Constants.ROWS][Constants.COLS];
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
                isMined[x][y] = true;
                i++;
            }
        }

    }
}