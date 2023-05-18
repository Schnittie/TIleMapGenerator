package de.schnittie.database;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RuleCreationGUI extends JFrame implements ActionListener {

    private final JButton fitsButton;
    private final JButton doesnFitsButton;
    private final JButton revertLastRuleButton;
    private final Square squareTopLeft;
    private final Square squareTopRight;
    private final Square squareDownLeft;
    private final Square squareDownRight;
    private RuleCreationTO currentRule;
    private List<Integer> lastRule;

    private final DBinteractions dbInteractions = DBinteractions.getInstance();

    public RuleCreationGUI() {
        super("RuleCreationGUI");
        // sure? i opened it from the main window - wanted to close it to get back there and the app just terminated
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel tilesPanel = new JPanel(new GridLayout(2, 2));
        squareTopLeft = new Square();
        squareTopRight = new Square();
        squareDownLeft = new Square();
        squareDownRight = new Square();
        tilesPanel.add(squareTopLeft);
        tilesPanel.add(squareTopRight);
        tilesPanel.add(squareDownLeft);
        tilesPanel.add(squareDownRight);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        fitsButton = new JButton("Fits");
        fitsButton.addActionListener(this);
        doesnFitsButton = new JButton("Doesn't fit");
        doesnFitsButton.addActionListener(this);
        revertLastRuleButton = new JButton("revert");
        revertLastRuleButton.addActionListener(this);
        buttonPanel.add(fitsButton);
        buttonPanel.add(doesnFitsButton);
        buttonPanel.add(revertLastRuleButton);

        mainPanel.add(tilesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        RuleCreationGUI gui = new RuleCreationGUI();
        try {
            gui.updateTiles();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoMoreRulesException e) {
            //DO nothing
        }

    }

    private void updateTiles() throws SQLException, NoMoreRulesException {
        squareTopLeft.reset();
        squareTopRight.reset();
        squareDownRight.reset();
        squareDownLeft.reset();

        currentRule = dbInteractions.getNewRule();
        switch (currentRule.next_to()) {
            case 1 -> {
                squareTopLeft.fillWithImage(currentRule.this_TileFilepath());
                squareDownLeft.fillWithImage(currentRule.that_TileFilepath());
            }
            case 2 -> {
                squareDownLeft.fillWithImage(currentRule.this_TileFilepath());
                squareTopLeft.fillWithImage(currentRule.that_TileFilepath());
            }
            case 3 -> {
                squareTopRight.fillWithImage(currentRule.this_TileFilepath());
                squareTopLeft.fillWithImage(currentRule.that_TileFilepath());
            }
            case 4 -> {
                squareTopLeft.fillWithImage(currentRule.this_TileFilepath());
                squareTopRight.fillWithImage(currentRule.that_TileFilepath());
            }
            case 5 -> {
                squareTopLeft.fillWithImage(currentRule.this_TileFilepath());
                squareDownRight.fillWithImage(currentRule.that_TileFilepath());
            }
            case 6 -> {
                squareTopRight.fillWithImage(currentRule.this_TileFilepath());
                squareDownLeft.fillWithImage(currentRule.that_TileFilepath());
            }
            case 7 -> {
                squareDownLeft.fillWithImage(currentRule.this_TileFilepath());
                squareTopRight.fillWithImage(currentRule.that_TileFilepath());
            }
            case 8 -> {
                squareDownRight.fillWithImage(currentRule.this_TileFilepath());
                squareTopLeft.fillWithImage(currentRule.that_TileFilepath());
            }
            default -> throw new RuntimeException("no Direction found for direction id: " + currentRule.next_to());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == revertLastRuleButton) {
            dbInteractions.revertRule(lastRule);
        } else {
            try {
                if (e.getSource() == fitsButton || e.getSource() == doesnFitsButton) {
                    lastRule = dbInteractions.putRuleIntoDB(currentRule.this_Tile(), currentRule.next_to(), currentRule.that_Tile(),
                            e.getSource() == fitsButton);

                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            updateTiles();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } catch (NoMoreRulesException exception) {
            System.out.println("no more rules left, you're done!");
        }

    }
}

class Square extends JPanel {
    private BufferedImage image;

    public Square() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void fillWithImage(String filepath) {
        try {
            image = ImageIO.read(new File(filepath));
            repaint();
        } catch (IOException ex) {
            System.out.println("Error loading image from file: " + filepath);
            ex.printStackTrace();
        }
    }

    public void reset() {
        image = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }


    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }
}

