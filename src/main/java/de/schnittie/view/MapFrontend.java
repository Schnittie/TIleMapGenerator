package de.schnittie.view;

import de.schnittie.model.mvcStuffs.GenerationErrorEvent;
import de.schnittie.model.mvcStuffs.MapGeneratorEvent;
import de.schnittie.model.mvcStuffs.NewMapEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MapFrontend extends JFrame implements ModelListener{
    private JScrollPane panel;
    private Container pane;
    public MapFrontend(ActionListener listener){
        super("MapGenerator");

        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JButton generateButton = new JButton("Generate Map");
        generateButton.addActionListener(listener);

        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> {
            JFileChooser chooseNewDirectory = new JFileChooser();
            chooseNewDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = chooseNewDirectory.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String chosenDirectory = chooseNewDirectory.getSelectedFile().getAbsolutePath();
            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(generateButton);
        buttonPanel.add(browseButton);

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        ((JPanel) getContentPane()).setOpaque(false);
        getRootPane().setOpaque(false);

        add(buttonPanel, BorderLayout.SOUTH);
        setSize(300,300);
        setVisible(true);
    }

    @Override
    public void update(MapGeneratorEvent event) {
       if (event.getClass().equals(NewMapEvent.class)){
           panel = new JScrollPane(new ImagePanel(((NewMapEvent) event).getImage())) ;
           panel.paint(((NewMapEvent) event).getImage().createGraphics());

           pane.add(panel, BorderLayout.CENTER);
           pack();
       }
       if (event.getClass().equals(GenerationErrorEvent.class)){
           JLabel label = new JLabel(((GenerationErrorEvent) event).getErrorMessage());
           pane.add(label);
           pack();
       }
    }
}
