package de.schnittie.controller;

import de.schnittie.model.mvcStuffs.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontendListener implements ActionListener {
    private Model model;

//TODO
    // This class is pretty useless... i mean: you have a single use of this, why not just lambda?!
    public FrontendListener(Model model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("action Performend");
        model.generateMap();
    }
}
