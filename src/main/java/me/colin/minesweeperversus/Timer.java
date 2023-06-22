package me.colin.minesweeperversus;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer extends JLabel {

    public Timer() {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);

        s = 0;
    }

    static int s = 0;

    public void tick() {
        s++;
        super.setText("Time: "+s);
    }
}
