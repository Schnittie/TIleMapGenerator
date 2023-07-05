package de.schnittie;

import de.schnittie.controller.Controller;
import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.database.InstallationHandler;

public class Main {
    public static void main(String[] args) {
        long timeBefore = System.currentTimeMillis();
        InstallationHandler.generateTilesForDefaultMapIfNotPresent();
        Configuration.reloadConfiguration();
        System.out.println(System.currentTimeMillis() - timeBefore); //162729 | 144709
        Controller controller = new Controller();
    }
}
