package de.schnittie.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable probabilityTable,Object value, boolean isSelected, boolean hasFocus, int row, int column){
        if (value instanceof BufferedImage){
            Image image = ((BufferedImage) value).getScaledInstance(30,30,Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(image));
        } else {
            setIcon(null);
        }
        setText(null);
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}
