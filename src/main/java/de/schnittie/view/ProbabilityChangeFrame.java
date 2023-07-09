package de.schnittie.view;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        DefaultTableModel probabilityTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

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

        JLabel infoText = new JLabel("Input new probability");
        infoText.setHorizontalAlignment(SwingConstants.CENTER);
        infoText.setFont(infoText.getFont().deriveFont(Font.BOLD,20f));

        probabilityTable.getColumnModel().getColumn(1).setCellRenderer(new EditableCellRenderer());
        probabilityTable.getColumnModel().getColumn(1).setCellEditor(new EditableCellEditor());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(infoText, BorderLayout.NORTH);
        panel.add(new JScrollPane(probabilityTable), BorderLayout.CENTER);

        getContentPane().add(panel);

        probabilityTableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 1) {
                    BufferedImage image = (BufferedImage) probabilityTable.getValueAt(row,0);
                    Integer id = getKeyByValue(image, iDtoImage);
                    Integer newProbability = (Integer) probabilityTable.getValueAt(row, column);
                    iDToProbabilityMap.put(id, newProbability);
                }
            }
        });
    }

    private static Integer getKeyByValue(BufferedImage image, HashMap<Integer, BufferedImage> iDToImage){
        for (Map.Entry<Integer, BufferedImage> entry : iDToImage.entrySet()){
            if (entry.getValue().equals(image)){
                return entry.getKey();
            }
        }
        return null;
    }

    private static class EditableCellRenderer extends JTextField implements TableCellRenderer{
        public EditableCellRenderer() {
            setOpaque(true);
            setBorder(BorderFactory.createLineBorder(Color.BLUE));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            setText(value != null ? value.toString() : "");
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            return this;
        }
    }

    private static class EditableCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JTextField editorComponent;

        public EditableCellEditor() {
            editorComponent = new JTextField();
            editorComponent.setBorder(BorderFactory.createLineBorder(Color.BLUE)); // Set border color
            //enter key stops edit
            editorComponent.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopCellEditing();
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            String text = editorComponent.getText();
            if (text.isEmpty()) {
                return null;
            } else {
                return Integer.parseInt(text);
            }
        }

        private static class IntegerDocument extends PlainDocument {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }

                try {
                    Integer.parseInt(str);
                    super.insertString(offs, str, a);
                } catch (NumberFormatException e) {
                    // Not a valid integer, ignore the input
                }
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editorComponent.setText(value != null ? value.toString() : "");
            return editorComponent;
        }
    }

}