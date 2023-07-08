package de.schnittie.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import de.schnittie.model.businesscode.tile.TileDataProvider;

public class ProbabilityChangeFrame extends JDialog {

    public ProbabilityChangeFrame() {

        TileDataProvider tileDataProvider = new TileDataProvider();
        ArrayList<Integer> TileIDs = tileDataProvider.getPossibleTileIDs();
        // make buffered image out of ID



        JDialog probabilityChangeFrame = new JDialog();

        String[] columnNames = {"Tile", "Probability"};
        Object[][] data = {{TileIDs, "probability"}};
        DefaultTableModel probabilityTableModel = new DefaultTableModel(data, columnNames);
        JTable probabilityTable = new JTable(probabilityTableModel);

        setSize(400, 400);
        setLocationRelativeTo(null);
    }
}