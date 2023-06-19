package de.schnittie.controller;

import de.schnittie.model.mvcStuffs.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontendListener implements ActionListener {
    private Model model;

    public FrontendListener(Model model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("action Performend");
        model.generateMap(100,100);
    }
}
