package de.schnittie;

import de.schnittie.controller.Controller;
import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.splitting.BoardFusionException;
import de.schnittie.model.businesscode.board.splitting.InvalidDimensionException;
import de.schnittie.model.database.InstallationHandler;

public class Main {
    public static void main(String[] args) throws MapGenerationException, InvalidDimensionException, BoardFusionException {
        InstallationHandler.generateTilesForDefaultMapIfNotPresent();
        Configuration.reloadConfiguration();
        Controller controller = new Controller();
    }
}
