package de.schnittie.model.database.configurations;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultConfigurationService {
    private static void addTilemapToConfig(ConfigurationHolder config, String filename, RuleCreationInstruction ruleCreationInstruction) {
        ClassLoader classLoader = DefaultConfigurationService.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        config.inputStreamToFilenameMap().put(inputStream, filename);
        config.inputStreamToRotateInstructionMap().put(inputStream, ruleCreationInstruction);
    }

    public static ArrayList<ConfigurationHolder> getDefaultConfigurations() throws URISyntaxException {
        ClassLoader classLoader = DefaultConfigurationService.class.getClassLoader();
        ArrayList<ConfigurationHolder> defaultConfigurations = new ArrayList<>();
        //TODO make into Enum

        //integer >=RuleCreationInstruction.ROTATE_NONE means that colour Rules apply and the  int indicates the amount of rotations that should be applied
        //a negative integer means that neighbourhood rules should apply ant the amount translates into the Probability
        HashMap<InputStream, RuleCreationInstruction> fantasyFileToInstructions = new HashMap<>();
        
        ConfigurationHolder fantasyConfig = new ConfigurationHolder(new HashMap<>(), "FantasyConfig", new HashMap<>(),new HashMap<>());
        addTilemapToConfig(fantasyConfig, "fantasyLake.png" , RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyLake.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyCastleBlue.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyCastleRed.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyCastleDesertBlue.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyCastleDesertRed.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyBridgeVertical.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyBridgeHorizontal.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyBrickPath.png", RuleCreationInstruction.ROTATE_NONE);
        addTilemapToConfig(fantasyConfig,"fantasyRapunzelTower.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyRuinFour.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyRuinOne.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyRuinThree.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyRuinTwo.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasySpring.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyRotatablePaths.png", RuleCreationInstruction.ROTATE_THRICE);
        addTilemapToConfig(fantasyConfig,"fantasyOcean.png", RuleCreationInstruction.ROTATE_NONE);
        addTilemapToConfig(fantasyConfig,"fantasyDesertPath.png", RuleCreationInstruction.ROTATE_NONE);
        addTilemapToConfig(fantasyConfig,"fantasyOceanShip.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyRiver.png", RuleCreationInstruction.ROTATE_NONE);

        addTilemapToConfig(fantasyConfig,"fantasyLakeNoInflux.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyLakeNoInfluxMirrored.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyLakeSideInflux.png",RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyLakeSideInfluxMirrored.png",RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyLakeTopInflux.png", RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyLakeTopInfluxMirrored.png", RuleCreationInstruction.ADJACENCY_RULES);

        addTilemapToConfig(fantasyConfig,"fantasyRiverBridgeStoneHorizontal.png",RuleCreationInstruction.ADJACENCY_RULES);
        addTilemapToConfig(fantasyConfig,"fantasyRiverBridgeStoneVertical.png", RuleCreationInstruction.ADJACENCY_RULES);
        defaultConfigurations.add(fantasyConfig);


        ConfigurationHolder desertConfig = new ConfigurationHolder(new HashMap<>(), "DesertConfig", new HashMap<>(),new HashMap<>());
        addTilemapToConfig(desertConfig,"fantasyDesertPath.png", RuleCreationInstruction.ROTATE_NONE);
        addTilemapToConfig(desertConfig,"fantasyDesertRiver.png", RuleCreationInstruction.ROTATE_NONE);
        defaultConfigurations.add(desertConfig);

        ConfigurationHolder basicConfig = new ConfigurationHolder(new HashMap<>(), "BasicConfig", new HashMap<>(),new HashMap<>());
        addTilemapToConfig(basicConfig,"BasicTilemapRotateNone.png", RuleCreationInstruction.ROTATE_NONE);
        addTilemapToConfig(basicConfig,"BasicTilemapRotateOnce.png", RuleCreationInstruction.ROTATE_ONCE);
        addTilemapToConfig(basicConfig,"BasicTilemapRotateThrice.png", RuleCreationInstruction.ROTATE_THRICE);
        defaultConfigurations.add(basicConfig);
        return defaultConfigurations;
    }
}
