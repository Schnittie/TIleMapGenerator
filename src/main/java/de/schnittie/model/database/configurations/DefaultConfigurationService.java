package de.schnittie.model.database.configurations;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;

public class DefaultConfigurationService {
    public static HashSet<ConfigurationHolder> getDefaultConfigurations() throws URISyntaxException {
        ClassLoader classLoader = DefaultConfigurationService.class.getClassLoader();
        HashSet<ConfigurationHolder> defaultConfigurations = new HashSet<>(3);

        HashMap<File, Integer> fantasyFileToInstructions = new HashMap<>(2);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLake.png").toURI()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyCastle.png").toURI()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBridgeVertical.png").toURI()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBridgeHorizontal.png").toURI()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBrickPath.png").toURI()) , 0);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasySpring.png").toURI()) , -1);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRotatablePaths.png").toURI()) , 3);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyDesertPath.png").toURI()) , 0);
        //fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyWaves.png").toURI()) , 3);
        HashMap<Integer, Integer> fantasyProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                fantasyFileToInstructions, "FantasyConfig",fantasyProbabilityChange));

        HashMap<File , Integer> basicFileToInstructions = new HashMap<>(2);
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateNone.png").toURI()), 0);
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateOnce.png").toURI()), 1);
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateThrice.png").toURI()), 3);
        HashMap<Integer, Integer> basicProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                basicFileToInstructions, "BasicConfig", basicProbabilityChange));

        return defaultConfigurations;
    }
}
