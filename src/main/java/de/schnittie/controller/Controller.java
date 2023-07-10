package de.schnittie.controller;

import de.schnittie.model.mvcStuffs.Model;
import de.schnittie.view.MapFrontend;

public class Controller {
    private Model model;
    private MapFrontend view;

    // what is this all for? it barely does something?
    public Controller(){
        this.model = new Model();
        // see comment in class FrontendListener.
        // plus: you could all do this in the MapFrontend constructor. like EVERYTHING you do here.
        // make a empty constructor MapFrontend() and just instantiate the model and link the listeners there.
        // much easier and less dependencies to other classes.
        this.view = new MapFrontend(new FrontendListener(model));
        model.addListener(view);
    }

}
