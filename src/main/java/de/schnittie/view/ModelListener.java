package de.schnittie.view;

import de.schnittie.model.mvcStuffs.MapGeneratorEvent;

import java.awt.event.ActionEvent;

public interface ModelListener {
    void actionPerformed(ActionEvent e);

    void update(MapGeneratorEvent event);
}
