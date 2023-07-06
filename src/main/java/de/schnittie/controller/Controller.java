package de.schnittie.controller;

import de.schnittie.model.mvcStuffs.Model;
import de.schnittie.view.MapFrontend;

public class Controller {
    private Model model;
    private MapFrontend view;

    public Controller(){
        this.model = new Model();
        this.view = new MapFrontend(new FrontendListener(model));
        model.addListener(view);
    }

}
