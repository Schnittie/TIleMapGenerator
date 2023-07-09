package de.schnittie.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import de.schnittie.model.GettingInformationService;
import de.schnittie.view.ImageRenderer;

public class ProbabilityChangeFrame extends JDialog {

    public ProbabilityChangeFrame() {
        super();
        setSize(600, 600);
        setLocationRelativeTo(null);
        GettingInformationService iDtoProbability = new GettingInformationService();
        HashMap<Integer,Integer> iDToProbabilityMap = iDtoProbability.getTileIdToProbabilityMap();
        HashMap<Integer, BufferedImage> iDtoImage = iDtoProbability.getTileIdToBufferedImageMap();

        String[] columnNames = {"Tile", "Probability"};
        DefaultTableModel probabilityTableModel = new DefaultTableModel(columnNames, 0);

        for (Map.Entry<Integer, Integer> entry : iDToProbabilityMap.entrySet()){
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            BufferedImage image = iDtoImage.get(key);
            probabilityTableModel.addRow(new Object[]{image, value});
        }

        JTable probabilityTable = new JTable(probabilityTableModel);
        int rowHeight = 40;
        probabilityTable.setRowHeight(rowHeight);
        probabilityTable.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
        getContentPane().add(new JScrollPane(probabilityTable));

    }
}