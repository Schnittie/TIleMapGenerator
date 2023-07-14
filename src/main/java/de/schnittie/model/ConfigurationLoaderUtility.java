package de.schnittie.model;

import de.schnittie.model.logic.Configuration;
import de.schnittie.model.logic.tile.TileDataProvider;
import de.schnittie.model.database.DBinteractions;

import java.sql.SQLException;

public class ConfigurationLoaderUtility {
    private static DBinteractions dBinteractions = DBinteractions.getInstance();

    public static void loadConfiguration(String configurationPathString){
        try {
            dBinteractions.setDbFolder(configurationPathString);
        } catch (SQLException e) {
            System.out.println("Not a valid Configuration: no DB found");
            throw new RuntimeException(e);
        }
        Configuration.reloadConfiguration();
        TileDataProvider.reloadTileDataProvider();
    }
    public static void reloadConfiguration(){
        loadConfiguration(dBinteractions.getDbFolder());
    }
}
