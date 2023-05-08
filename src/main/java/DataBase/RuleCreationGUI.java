package DataBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RuleCreationGUI extends JFrame implements ActionListener {
    private JLabel thisTileLabel, thatTileLabel;
    private JButton fitsButton, doesnFitsButton;
    private RuleCreationTO currentRule;
    private DBinteractions dbInteractions;

    public RuleCreationGUI() throws SQLException {
        super("Create Rule");
        dbInteractions =  DBinteractions.getInstance();
        currentRule = dbInteractions.getNewRule();
        displayRule();
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void displayRule() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel tilesPanel = new JPanel(new FlowLayout());
        thisTileLabel = new JLabel(new ImageIcon(currentRule.this_TileFilepath()));
        thatTileLabel = new JLabel(new ImageIcon(currentRule.that_TileFilepath()));
        int nextTo = currentRule.next_to();
        switch (nextTo) {
            case 1 -> tilesPanel.add(thisTileLabel, BorderLayout.NORTH);
            case 2 -> tilesPanel.add(thisTileLabel, BorderLayout.SOUTH);
            case 3 -> tilesPanel.add(thisTileLabel, BorderLayout.EAST);
            case 4 -> tilesPanel.add(thisTileLabel, BorderLayout.WEST);
            case 5 -> {
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                tilesPanel.add(thisTileLabel, c);
            }
            case 6 -> {
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                tilesPanel.add(thisTileLabel, c);
            }
            case 7 -> {
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                tilesPanel.add(thisTileLabel, c);
            }
            case 8 -> {
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 1;
                tilesPanel.add(thisTileLabel, c);
            }
        }
        tilesPanel.add(thatTileLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        fitsButton = new JButton("Fits");
        fitsButton.addActionListener(this);
        doesnFitsButton = new JButton("Doesn't fit");
        doesnFitsButton.addActionListener(this);
        buttonPanel.add(fitsButton);
        buttonPanel.add(doesnFitsButton);

        mainPanel.add(tilesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        boolean canItBe = false;
        if (e.getSource() == fitsButton) {
            canItBe = true;
        }
        try {
            dbInteractions.putRuleIntoDB(currentRule.this_Tile(), currentRule.next_to(), currentRule.that_Tile(), canItBe);
            currentRule = dbInteractions.getNewRule();
            getContentPane().removeAll();
            displayRule();
            revalidate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) throws SQLException {
        RuleCreationGUI gui = new RuleCreationGUI();
    }
}

