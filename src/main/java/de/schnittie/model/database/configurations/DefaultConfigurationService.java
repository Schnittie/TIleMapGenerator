package de.schnittie.model.database.configurations;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultConfigurationService {
    public static ArrayList<ConfigurationHolder> getDefaultConfigurations() throws URISyntaxException {
        ClassLoader classLoader = DefaultConfigurationService.class.getClassLoader();
        ArrayList<ConfigurationHolder> defaultConfigurations = new ArrayList<>();
        //TODO make into Enum

        //integer >=RuleCreationInstruction.ROTATE_NONE means that colour Rules apply and the  int indicates the amount of rotations that should be applied
        //a negative integer means that neighbourhood rules should apply ant the amount translates into the Probability
        HashMap<File, RuleCreationInstruction> fantasyFileToInstructions = new HashMap<>();
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLake.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyCastleBlue.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyCastleRed.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyCastleDesertBlue.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyCastleDesertRed.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBridgeVertical.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBridgeHorizontal.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyBrickPath.png").getFile()) , RuleCreationInstruction.ROTATE_NONE);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRapunzelTower.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRuinFour.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRuinOne.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRuinThree.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRuinTwo.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasySpring.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRotatablePaths.png").getFile()) , RuleCreationInstruction.ROTATE_THRICE);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyOcean.png").getFile()) , RuleCreationInstruction.ROTATE_NONE);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyDesertPath.png").getFile()) , RuleCreationInstruction.ROTATE_NONE);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyOceanShip.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRiver.png").getFile()) , RuleCreationInstruction.ROTATE_NONE);

        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLakeNoInflux.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLakeNoInfluxMirrored.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLakeSideInflux.png").getFile()) ,RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLakeSideInfluxMirrored.png").getFile()) ,RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLakeTopInflux.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyLakeTopInfluxMirrored.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);

        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRiverBridgeStoneHorizontal.png").getFile()) ,RuleCreationInstruction.ADJACENCY_RULES);
        fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyRiverBridgeStoneVertical.png").getFile()) , RuleCreationInstruction.ADJACENCY_RULES);
        //fantasyFileToInstructions.put(new File(classLoader.getResource("fantasyWaves.png").getFile()) , RuleCreationInstruction.ROTATE_THRICE);
        HashMap<Integer, Integer> fantasyProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                fantasyFileToInstructions, "FantasyConfig",fantasyProbabilityChange));

        HashMap<File , RuleCreationInstruction> desertFileToInstructions = new HashMap<>();
        desertFileToInstructions.put(new File(classLoader.getResource("fantasyDesertPath.png").getFile()), RuleCreationInstruction.ROTATE_NONE);
        desertFileToInstructions.put(new File(classLoader.getResource("fantasyDesertRiver.png").getFile()), RuleCreationInstruction.ROTATE_NONE);
        //desertFileToInstructions.put(new File(classLoader.getResource("fantasyCastleDesertBlue.png").getFile()), RuleCreationInstruction.ADJACENCY_RULES);
//        desertFileToInstructions.put(new File(classLoader.getResource("fantasyCastleDesertRed.png").getFile()), RuleCreationInstruction.ADJACENCY_RULES);
        HashMap<Integer, Integer> desertProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                desertFileToInstructions, "DesertConfig", desertProbabilityChange));;

        HashMap<File , RuleCreationInstruction> basicFileToInstructions = new HashMap<>();
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateNone.png").getFile()), RuleCreationInstruction.ROTATE_NONE);
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateOnce.png").getFile()), RuleCreationInstruction.ROTATE_ONCE);
        basicFileToInstructions.put(new File(classLoader.getResource("BasicTilemapRotateThrice.png").getFile()), RuleCreationInstruction.ROTATE_THRICE);
        HashMap<Integer, Integer> basicProbabilityChange = new HashMap<>();
        defaultConfigurations.add(new ConfigurationHolder(
                basicFileToInstructions, "BasicConfig", basicProbabilityChange));;

        return defaultConfigurations;
    }
}
