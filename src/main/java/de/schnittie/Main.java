package de.schnittie;

import de.schnittie.controller.Controller;
import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.database.InstallationHandler;

public class Main {
    public static void main(String[] args) {
        //uncomment this to run normally
        InstallationHandler.generateTilesForDefaultMapIfNotPresent();
        Configuration.ReloadConfiguration();
        Controller controller = new Controller();

        //uncomment this to install and generate Rules
        //Model model = new Model();

//          //Uncomment this if your Name starts with L
//            model.addTiles("C:\\Users\\laure\\Desktop\\TilesTestingSockets\\Tilemap.png", 3);
//            model.addTiles("C:\\Users\\laure\\Desktop\\TilesTestingSockets\\TilemapNonRotate.png", 0);
//            model.addTiles("C:\\Users\\laure\\Desktop\\TilesTestingSockets\\DefaultTilemapRotateOnce.png", 1);
        //model.addTiles("C:\\Users\\laure\\Pictures\\Tiles\\Castle.png", 0);

//
        //uncomment this if your Name starts with E
//            model.addTiles("C:\Users\eddi3\OneDrive\Dokumente\Uni\FemboyTileMaps\TilesTestingSockets\\Tilemap.png", 3);
//            model.addTiles("C:\Users\eddi3\OneDrive\Dokumente\Uni\FemboyTileMaps\TilesTestingSockets\\TilemapNonRotate.png", 0);
//            model.addTiles("C:\Users\eddi3\OneDrive\Dokumente\Uni\FemboyTileMaps\TilesTestingSockets\\DefaultTilemapRotateOnce.png", 1);
        //model.generateRules();
//
//        model.generateMap(100,100);


    }
}
