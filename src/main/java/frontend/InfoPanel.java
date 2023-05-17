package frontend;

import businesscode.Board;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private Board board;
    private final JLabel percentageLabel, heightLabel, widthLabel;
    private volatile boolean generationInProgress;

    public InfoPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        percentageLabel = new JLabel();
        heightLabel = new JLabel();
        widthLabel = new JLabel();

        //mainPanel.add(percentageLabel);
        add(heightLabel);
        add(widthLabel);

        setVisible(true);
    }

    public void infoForGeneration(Board board) {
        this.board = board;
        updateInfos();

    }

    public void updateInfos() {
       // percentageLabel.setText(Math.divideExact(board.getAmountCollapsed() * 100, HEIGHT * WIDTH) + "%");
        heightLabel.setText("" + board.getHEIGHT());
        widthLabel.setText("" + board.getWIDTH());
    }

}
