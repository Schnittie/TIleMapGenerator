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
        //TODO make into Enum

        //integer >=0 means that colour Rules apply and the  int indicates the amount of rotations that should be applied
        //a negative integer means that neighbourhood rules should apply ant the amount translates into the Probability
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
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyOcean.png").getFile()) , 3);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyDesertPath.png").getFile()) , 0);
        //fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyWaves.png").getFile()) , 3);
        HashMap<Integer, Integer> fantasyProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                fantasyFileToInstructions, "FantasyConfig",fantasyProbabilityChange));

        HashMap<File , Integer> basicFileToInstructions = new HashMap<>(2);
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateNone.png").getFile()), 0);
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateOnce.png").getFile()), 1);
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateThrice.png").getFile()), 3);
        HashMap<Integer, Integer> basicProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                basicFileToInstructions, "BasicConfig", basicProbabilityChange));

        return defaultConfigurations;
    }
}
