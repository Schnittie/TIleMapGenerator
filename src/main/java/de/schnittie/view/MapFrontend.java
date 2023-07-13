package de.schnittie.view;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.database.InstallationHandler;
import de.schnittie.model.mvcStuffs.GenerationErrorEvent;
import de.schnittie.model.mvcStuffs.MapGeneratorEvent;
import de.schnittie.model.mvcStuffs.Model;
import de.schnittie.model.mvcStuffs.NewMapEvent;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import static de.schnittie.model.ConfigurationLoaderService.loadConfiguration;


public class MapFrontend extends JFrame implements ModelListener{
    private JScrollPane panel;
    private Container pane;
    private Model model;
    private JTextField widthTextField;
    private JTextField heightTextField;
    private JLabel warningLabel;

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("action Performend");
        model.generateMap();
    }
    public MapFrontend(){
        super("MapGenerator");

        model = new Model();
        model.addListener(this);

        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JLabel widthLabel = new JLabel("Width:");
        widthTextField = new JTextField(10);

        JLabel heightLabel = new JLabel("Height:");
        heightTextField = new JTextField(10);

        ((AbstractDocument) widthTextField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
        ((AbstractDocument) heightTextField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());

        warningLabel = new JLabel();

        JButton generateButton = new JButton("Generate Map");
        generateButton.addActionListener(e -> {
            System.out.println("Action performed");

            int width = Integer.parseInt(widthTextField.getText());
            int height = Integer.parseInt(heightTextField.getText());

            model.generateMap(width, height);
        });

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
        buttonPanel.add(widthLabel);
        buttonPanel.add(widthTextField);
        buttonPanel.add(heightLabel);
        buttonPanel.add(heightTextField);
        buttonPanel.add(generateButton);
        buttonPanel.add(warningLabel);
        buttonPanel.add(configButton);
        buttonPanel.add(probabilityButton);
        warningLabel.setText("Warning: Generating a map with high numbers may take a long time.");

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        ((JPanel) getContentPane()).setOpaque(false);
        getRootPane().setOpaque(false);

        add(buttonPanel, BorderLayout.SOUTH);
        //add progressBar
        setSize(1000,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void update(MapGeneratorEvent event) {
       if (event.getClass() == NewMapEvent.class){

           if (panel != null) {
               pane.remove(panel);
           }

           panel = new JScrollPane(new ImagePanel(((NewMapEvent) event).getImage())) ;
           panel.paint(((NewMapEvent) event).getImage().createGraphics());
           panel.setPreferredSize(new Dimension(700,700));
           panel.getVerticalScrollBar().setUnitIncrement(10);
           panel.getHorizontalScrollBar().setUnitIncrement(10);
           int horizontalScrollValue = (panel.getHorizontalScrollBar().getMaximum() - panel.getHorizontalScrollBar().getVisibleAmount()) * 2;
           panel.getHorizontalScrollBar().setValue(horizontalScrollValue);
           int verticalScrollValue = (panel.getVerticalScrollBar().getMaximum() - panel.getVerticalScrollBar().getMinimum()) / 4;
           panel.getVerticalScrollBar().setValue(verticalScrollValue);

           pane.add(panel, BorderLayout.CENTER);
           pack();
           setLocationRelativeTo(null);
       }
       if (event.getClass() == GenerationErrorEvent.class){
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
                        System.getProperty("file.separator") + "Local" + System.getProperty("file.separator") + "CatboyMaps"
                        + System.getProperty("file.separator") + "TileMapGenerator"));
        chooseNewDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = chooseNewDirectory.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String chosenDirectory = chooseNewDirectory.getSelectedFile().getAbsolutePath();
            System.out.println(chosenDirectory);
            loadConfiguration(chosenDirectory);
        }
    }

    public static void main(String[] args)  {
        InstallationHandler.generateTilesForDefaultMapIfNotPresent();
        Configuration.reloadConfiguration();

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
               IllegalAccessException e) {
            e.printStackTrace();
        }

        MapFrontend mapFrontend = new MapFrontend();
    }

}
