package de.schnittie.view;

import javax.swing.*;
import java.awt.*;

//no use yet but maybe framework for progress bar?
public class ProgressBar extends JFrame {
    private JProgressBar progressBar;
    private int totalTiles = 200; // how to get total tiles?
    private int completedTiles = 0; // how to get completed tiles? is that even possible?

    public ProgressBar(){
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        progressBar.setForeground(Color.GREEN);
        progressBar.setBackground(Color.BLACK);

        getContentPane().add(progressBar,BorderLayout.CENTER);

        showProgress();
    }
    private void showProgress() {
        while (completedTiles < totalTiles) {
            progressBar.setValue(calculateProgress());
        }
        progressBar.setString("Done!");
    }

    private int calculateProgress(){
        double progress = (double) completedTiles / totalTiles;
        return (int) (progress * 100);
    }
}
