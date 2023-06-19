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
    public static void main(String[] args) {
        Controller controller = new Controller();
//        Model model = new Model();
//        if (true) {
//            model.addTiles("C:\\Users\\laure\\Desktop\\TilesTestingSockets\\Tilemap.png", 3);
//            model.addTiles("C:\\Users\\laure\\Desktop\\TilesTestingSockets\\TilemapNonRotate.png", 0);
//            model.addTiles("C:\\Users\\laure\\Desktop\\TilesTestingSockets\\6Brown0Green3Blue.png", 1);
//            model.generateRules();
//        }
//        model.generateMap(100,100);
    }
}
