package de.schnittie;

import de.schnittie.controller.Controller;
import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.database.InstallationHandler;

public class Main {
    public static void main(String[] args) {
        InstallationHandler.generateTilesForDefaultMapIfNotPresent();
        Configuration.ReloadConfiguration();
        Controller controller = new Controller();
    }
}
