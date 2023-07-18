package me.colin.minesweeperversus;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ClickedMine {

    public ClickedMine() {
        infoBarRed();
        shake();
    }

    static Thread thread;

    void infoBarRed() {
        Color colour = Color.decode("#4A752C");
        Color oldColour = Color.RED;
        Main.panel.setBackground(oldColour);
        int FadeSteps = 75;
        final int dRed = colour.getRed() - oldColour.getRed();
        final int dGreen = colour.getGreen() - oldColour.getGreen();
        final int dBlue = colour.getBlue() - oldColour.getBlue();
        if(thread != null) thread.interrupt(); //TODO end loop/thread safely
        thread = new Thread(() -> { // Make a separate thread to not freeze the main thread
            for (int i = 0; i <= FadeSteps; i++) {
                final Color c = new Color(
                        oldColour.getRed() + ((dRed * i) / FadeSteps),
                        oldColour.getGreen() + ((dGreen * i) / FadeSteps),
                        oldColour.getBlue() + ((dBlue * i) / FadeSteps));
                Main.panel.setBackground(c);
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


    void shake() {
        JFrame frame = Main.jFrame;
        Point currLocation = frame.getLocationOnScreen();

        for (int i = 0; i < 5; i++) {
            int iDisplaceXBy = new Random().nextInt(50) - 25;
            int iDisplaceYBy = new Random().nextInt(50) - 25;
            Point position = new Point(currLocation.x + iDisplaceXBy, currLocation.y
                    + iDisplaceYBy);
            frame.setLocation(position);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frame.setLocation(currLocation);
    }
}
