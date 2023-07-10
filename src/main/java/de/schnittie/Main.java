package de.schnittie;

import de.schnittie.controller.Controller;
import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.database.InstallationHandler;

public class Main {
    //TODO
    // this class is kind of useless... see comments in class Controller
    // I'd say what you want is moving the main function and the stuff happening in the Controller constructor to your
    // main JFrame and start everything there.
    public static void main(String[] args)  {
        InstallationHandler.generateTilesForDefaultMapIfNotPresent();
        Configuration.reloadConfiguration();

        Controller controller = new Controller();
    }
}
