package me.colin.minesweeperversus.info;

import me.colin.minesweeperversus.fonts.Roboto;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Score extends JLabel {

    static int score;

    public Score() {
        score = 0;
        try {
            ImageIcon icon = new ImageIcon(new URL("https://www.google.com/logos/fnbx/snake_arcade/v3/trophy_00.png"));
            Image image = icon.getImage(); // transform it
            ImageIcon icon2 = new ImageIcon(image.getScaledInstance(50, 50,  Image.SCALE_SMOOTH)); // scale it the smooth way
            super.setIcon(icon2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        super.setHorizontalTextPosition(RIGHT);
        super.setFont(Roboto.REGULAR.deriveFont(30f));
        super.setForeground(Color.WHITE);
        update();
    }

    public void win() {
        score++;
        this.update();

    }

    void update() {
        super.setText(""+score);
    }

}
