package de.schnittie.model.database.configurations;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DefaultConfigurationService {
    public static ArrayList<ConfigurationHolder> getDefaultConfigurations() throws URISyntaxException {
        ClassLoader classLoader = DefaultConfigurationService.class.getClassLoader();
        ArrayList<ConfigurationHolder> defaultConfigurations = new ArrayList<>(3);

        HashMap<File, Integer> fantasyFileToInstructions = new HashMap<>(2);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLake.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("FantasyCastle.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBridgeVertical.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBridgeHorizontal.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBrickPath.png").getFile()) , 0);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRapunzelTower.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRuinFour.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRuinOne.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRuinThree.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRuinTwo.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasySpring.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRotatablePaths.png").getFile()) , 3);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyOcean.png").getFile()) , 0);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyDesertPath.png").getFile()) , 0);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyOceanShip.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyOceanShip.png").getFile()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRiver.png").getFile()) , 0);
        //fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyWaves.png").getFile()) , 3);
        HashMap<Integer, Integer> fantasyProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                fantasyFileToInstructions, "FantasyConfig",fantasyProbabilityChange));



        return defaultConfigurations;
    }
}
