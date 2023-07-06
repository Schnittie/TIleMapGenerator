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
        fantasyFileToInstructions.put(new File(classLoader.getResource("TestTiles_V01.png").toURI()) , 0);
        fantasyFileToInstructions.put(new File(classLoader.getResource("Tiles.png").toURI()) , -1);
        HashMap<Integer, Integer> fantasyProbabilityChange = new HashMap<>();
        fantasyProbabilityChange.put(114,1000);
        defaultConfigurations.add(new ConfigurationHolder(
                fantasyFileToInstructions, "FantasyConfig",fantasyProbabilityChange));

        HashMap<File , Integer> basicFileToInstructions = new HashMap<>(2);
        basicFileToInstructions.put(new File(classLoader.getResource("DefaultTilemapRotateNone.png").toURI()), 0);
        basicFileToInstructions.put(new File(classLoader.getResource("DefaultTilemapRotateOnce.png").toURI()), 1);
        basicFileToInstructions.put(new File(classLoader.getResource("DefaultTilemapRotateThrice.png").toURI()), 3);
        HashMap<Integer, Integer> basicProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                basicFileToInstructions, "BasicConfig", basicProbabilityChange));

        return defaultConfigurations;
    }
}
