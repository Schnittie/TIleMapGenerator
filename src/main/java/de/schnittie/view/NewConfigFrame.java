package de.schnittie.view;

import de.schnittie.model.database.configurations.CreatingConfigurationUtility;
import de.schnittie.model.database.configurations.RuleCreationInstruction;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class NewConfigFrame extends JDialog {

    private JButton saveButton;
    private JButton addTileButton;
    private JButton addConfigButton;
    private JTextField configNameField;
    private CreatingConfigurationUtility configCreater;

    public NewConfigFrame() {
        super();
        setSize(600, 600);
        setLocationRelativeTo(null);
        configNameField = new JTextField(10);
        saveButton = new JButton("Save new config");
        saveButton.addActionListener(e -> saveChanges());

        addConfigButton = new JButton("add Config");
        addConfigButton.addActionListener(e -> newConfig());

        addTileButton = new JButton("add Tile");
        addTileButton.addActionListener(e -> newTile());

        JPanel panel = new JPanel(new FlowLayout()); // Changed to FlowLayout
        panel.add(saveButton);
        panel.add(addTileButton);
        panel.add(addConfigButton);
        panel.add(configNameField);

        getContentPane().add(panel);


    }
    private void newTile() {
        JFileChooser chooseNewFileMap = new JFileChooser();
        chooseNewFileMap.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = chooseNewFileMap.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String chosenFile = chooseNewFileMap.getSelectedFile().getAbsolutePath();
            System.out.println(chosenFile);
            //Only rotate none possible for now
            configCreater.addTileMapToConfig(chosenFile, RuleCreationInstruction.ROTATE_NONE);
        }
    }

    private void newConfig() {
        if (configNameField.getText().isEmpty() || configNameField.getText().isBlank()){
            return;
        }
        configCreater = new CreatingConfigurationUtility(configNameField.getText());
    }

    private void saveChanges() {
        configCreater.finishNewConfig();

    }

    private static Integer getKeyByValue(BufferedImage image, HashMap<Integer, BufferedImage> iDToImage){
        for (Map.Entry<Integer, BufferedImage> entry : iDToImage.entrySet()){
            if (entry.getValue().equals(image)){
                return entry.getKey();
            }
        }
        return null;
    }

}