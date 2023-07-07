package de.schnittie.view;

import de.schnittie.model.mvcStuffs.GenerationErrorEvent;
import de.schnittie.model.mvcStuffs.MapGeneratorEvent;
import de.schnittie.model.mvcStuffs.NewMapEvent;
import de.schnittie.view.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

import static de.schnittie.model.ConfigurationLoaderService.loadConfiguration;


public class MapFrontend extends JFrame implements ModelListener{
    private JScrollPane panel;
    private Container pane;
    public MapFrontend(ActionListener listener){
        super("MapGenerator");

        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JButton generateButton = new JButton("Generate Map");
        generateButton.addActionListener(listener);

        JButton configButton = new JButton("Load new Config");
        configButton.addActionListener(e -> {
            generateButton.setEnabled(false);
            JFileChooser chooseNewDirectory = new JFileChooser();
            chooseNewDirectory.setCurrentDirectory(new File(
                    System.getProperty("user.home") + System.getProperty("file.separator") + "AppData" +
                    System.getProperty("file.separator") + "Local" + System.getProperty("file.separator") + "CatboyMaps"));
            chooseNewDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = chooseNewDirectory.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String chosenDirectory = chooseNewDirectory.getSelectedFile().getAbsolutePath();
                System.out.println(chosenDirectory);
                loadConfiguration(chosenDirectory);
            }
            generateButton.setEnabled(true);
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(generateButton);
        buttonPanel.add(configButton);

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        ((JPanel) getContentPane()).setOpaque(false);
        getRootPane().setOpaque(false);

        add(buttonPanel, BorderLayout.SOUTH);
        //add progressBar
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
