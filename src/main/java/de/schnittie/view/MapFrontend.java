package de.schnittie.view;

import de.schnittie.model.mvcStuffs.GenerationErrorEvent;
import de.schnittie.model.mvcStuffs.MapGeneratorEvent;
import de.schnittie.model.mvcStuffs.NewMapEvent;
import de.schnittie.view.ProbabilityChangeFrame;

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
            openConfiguration();
            generateButton.setEnabled(true);
        });

        JButton probabilityButton = new JButton("Change Probability");
        probabilityButton.addActionListener(e -> {
            generateButton.setEnabled(false);
            configButton.setEnabled(false);
            ProbabilityChangeFrame probabilityChangeFrame = new ProbabilityChangeFrame();
            probabilityChangeFrame.setVisible(true);
            generateButton.setEnabled(true);
            configButton.setEnabled(true);
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(generateButton);
        buttonPanel.add(configButton);
        buttonPanel.add(probabilityButton);

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        ((JPanel) getContentPane()).setOpaque(false);
        getRootPane().setOpaque(false);

        add(buttonPanel, BorderLayout.SOUTH);
        //add progressBar
        setSize(600,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void update(MapGeneratorEvent event) {
       if (event.getClass().equals(NewMapEvent.class)){
           panel = new JScrollPane(new ImagePanel(((NewMapEvent) event).getImage())) ;
           panel.paint(((NewMapEvent) event).getImage().createGraphics());

           pane.add(panel, BorderLayout.CENTER);
           pack();
           setLocationRelativeTo(null);
       }
       if (event.getClass().equals(GenerationErrorEvent.class)){
           JLabel label = new JLabel(((GenerationErrorEvent) event).getErrorMessage());
           pane.add(label);
           pack();
       }
    }

    private void openConfiguration() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){
            e.printStackTrace();
        }
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
    }
}
