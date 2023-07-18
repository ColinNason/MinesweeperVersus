package me.colin.minesweeperversus.info;

import me.colin.minesweeperversus.fonts.Roboto;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer extends JLabel {

    public Timer() {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);

        try {
            ImageIcon icon = new ImageIcon(new URL("https://www.google.com/logos/fnbx/minesweeper/clock_icon.png"));
            Image image = icon.getImage(); // transform it
            ImageIcon icon2 = new ImageIcon(image.getScaledInstance(50, 50,  Image.SCALE_SMOOTH)); // scale it the smooth way
            super.setIcon(icon2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        super.setHorizontalTextPosition(RIGHT);
        super.setFont(Roboto.REGULAR.deriveFont(30f));
        super.setForeground(Color.WHITE);

        s = 0;
    }

    static int s = 0;

    public void tick() {
        s++;
        super.setText(String.valueOf(s));
    }
}
