package me.colin.minesweeperversus;

import java.util.Random;

public class MineMap {
    // package access
    int numMines;
    boolean[][] isMined = new boolean[Board.ROWS][Board.COLS];
    // default is false

    // Constructor
    public MineMap() {
        super();
    }

    // Allow user to change the rows and cols
    public void newMineMap(int numMines) {
        this.numMines = numMines;
        // Hardcoded for illustration and testing, assume numMines=10

        for(int i = 0; i < numMines;) {
            int x = new Random().nextInt(Board.ROWS);
            int y = new Random().nextInt(Board.COLS);

            if(!isMined[x][y]) {
                isMined[x][y] = true;
                i++;
            }
        }


//        isMined[0][0] = true;
//        isMined[5][2] = true;
//        isMined[9][5] = true;
//        isMined[6][7] = true;
//        isMined[8][2] = true;
//        isMined[2][4] = true;
//        isMined[5][7] = true;
//        isMined[7][7] = true;
//        isMined[3][6] = true;
//        isMined[4][8] = true;
    }
}