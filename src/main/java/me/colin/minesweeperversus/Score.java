package me.colin.minesweeperversus;

import javax.swing.*;

public class Score extends JLabel {

    static int score;

    public Score() {
        score = 0;
        update();
    }

    void win() {
        score++;
        this.update();
    }

    void update() {
        super.setText("Score: "+score);
    }

}
