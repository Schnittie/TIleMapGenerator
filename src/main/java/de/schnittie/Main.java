package de.schnittie;

import de.schnittie.controller.Controller;
import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.database.InstallationHandler;

public class Main {
    public static void main(String[] args)  {
        InstallationHandler.generateTilesForDefaultMapIfNotPresent();
        Configuration.reloadConfiguration();

        //ConfigurationLoaderService.loadConfiguration("C:\\Users\\laure\\AppData\\Local\\CatboyMaps\\TileMapGenerator\\FantasyConfig");
        // 500x500 FantasyMap in 427118 Milliseconds
        // 500x500 FantasyMap in 461686 Milliseconds
        // 500x500 FantasyMap in 469251 Milliseconds
        Controller controller = new Controller();
    }
}
