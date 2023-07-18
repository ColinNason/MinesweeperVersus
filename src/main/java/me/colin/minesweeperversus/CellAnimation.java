package me.colin.minesweeperversus;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class CellAnimation {


    public int currentRotation;
    public int rotation;
    public int posX;
    public int posY;
    public int velocityX;
    public int velocityY;
    public int size;
    Color color;
    public boolean toRemove;


    public CellAnimation(Cell cell) throws IOException {
        rotation = new Random().nextInt(6) - 3;
        velocityX = new Random().nextInt(10) - 5;
        velocityY = new Random().nextInt(10) * -1;
        posX = cell.getX();
        posY = cell.getY();
        color = cell.getColor(false);
        size = cell.getHeight();
        GameBoardPanel.animationList.add(this);

        Thread thread = new Thread(() -> { // Make a separate thread to not freeze the main thread
            while (size >= 0 && posY < ((Constants.ROWS + 1) * cell.getHeight()) && posY > -cell.getHeight() && posX < ((Constants.COLS + 1) * cell.getWidth()) && posX > -cell.getWidth()) {

                posX += velocityX;
                posY += velocityY;

                currentRotation += rotation;

                velocityY++;

                size--;

                GameBoardPanel.update();

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //GameBoardPanel.animationList.remove(this);
            toRemove = true;
        });
        thread.start();
    }


}
