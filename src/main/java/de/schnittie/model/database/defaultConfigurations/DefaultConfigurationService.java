package de.schnittie.model.database.defaultConfigurations;

import java.util.HashMap;
import java.util.HashSet;

public class DefaultConfigurationService {
    public static HashSet<DefaultConfigurationHolder> getDefaultConfigurations(){
        HashSet<DefaultConfigurationHolder> defaultConfigurations = new HashSet<>(3);

        HashMap<String , Integer> fantasyFilePathsToInstructions = new HashMap<>(2);
        fantasyFilePathsToInstructions.put("TestTiles_V01.png", 0);
        fantasyFilePathsToInstructions.put("Tiles.png", -1);
        HashMap<Integer, Integer> fantasyProbabilityChange = new HashMap<>();
        fantasyProbabilityChange.put(114,1000);
        defaultConfigurations.add(new DefaultConfigurationHolder(
                fantasyFilePathsToInstructions, "FantasyConfig",fantasyProbabilityChange));

        HashMap<String , Integer> basicFilePathsToInstructions = new HashMap<>(2);
        basicFilePathsToInstructions.put("DefaultTilemapRotateNone.png", 0);
        basicFilePathsToInstructions.put("DefaultTilemapRotateOnce.png", 1);
        basicFilePathsToInstructions.put("DefaultTilemapRotateThrice.png", 3);
        HashMap<Integer, Integer> basicProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new DefaultConfigurationHolder(
                basicFilePathsToInstructions, "BasicConfig", basicProbabilityChange));

        return defaultConfigurations;
    }
}
