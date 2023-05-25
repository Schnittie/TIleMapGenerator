package de.schnittie.view;

import de.schnittie.model.GenerationErrorEvent;
import de.schnittie.model.MapGeneratorEvent;
import de.schnittie.model.NewMapEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class MapFrontend extends JFrame implements ModelListener{
    private JScrollPane panel;
    private Container pane;
    public MapFrontend(ActionListener listener){
        super("MapGenerator");

        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JButton button = new JButton("Generate Map");
        button.addActionListener(listener);


        pane.add(button, BorderLayout.SOUTH);
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
