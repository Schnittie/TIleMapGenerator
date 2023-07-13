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
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_2_0.png",60);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_3_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_4_0.png",1000);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_5_0.png",1000);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_6_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_7_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_8_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_9_0.png",150);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_0_0_0.png",5);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_0_1_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_0_2_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_0_3_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_0_4_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_0_5_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_0_6_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_0_7_0.png",0);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_1_0_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_2_0_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_2_1_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_2_3_0.png",0);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_3_0_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_3_1_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_3_2_0.png",5);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_4_0_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_3_3_0.png",0);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_4_2_0.png",60);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_4_3_0.png",0);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_5_0_0.png",5);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_5_1_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertRiver.png_5_2_0.png",5);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_0_2_0.png",5);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_0_3_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_0_4_0.png",30);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_0_5_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_0_6_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_0_7_0.png",30);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_1_1_0.png",0);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_1_2_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_1_3_0.png",0);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_1_4_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_1_5_0.png",600);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_1_6_0.png",600);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_1_7_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_2_1_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_2_2_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_2_3_0.png",600);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_2_4_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_2_5_0.png",100);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_2_6_0.png",100);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_2_7_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_0_0.png",5);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_1_0.png",60);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_1_0.png",60);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_2_0.png",60);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_3_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_4_0.png",30);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_5_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_6_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_7_0.png",30);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_3_9_0.png",75);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_4_0_0.png",60);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_4_1_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_4_2_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_4_3_0.png",0);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_4_4_0.png",200);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_4_5_0.png",200);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_4_6_0.png",400);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_4_9_0.png",100);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_0_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_1_0.png",0);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_2_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_3_0.png",40);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_4_0.png",200);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_5_0.png",200);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_6_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_7_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_8_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_5_9_0.png",45);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_0_0.png",50);
        changeProbabilityForConfig(desertConfig,"rotated_0_times_from_fantasyDesertPath.png_6_1_0.png",50);
        defaultConfigurations.add(desertConfig);

        ConfigurationHolder basicConfig = new ConfigurationHolder(new HashMap<>(), "BasicConfig", new HashMap<>(),new HashMap<>());
        addTilemapToConfig(basicConfig,"BasicTilemapRotateNone.png", RuleCreationInstruction.ROTATE_NONE);
        addTilemapToConfig(basicConfig,"BasicTilemapRotateOnce.png", RuleCreationInstruction.ROTATE_ONCE);
        addTilemapToConfig(basicConfig,"BasicTilemapRotateThrice.png", RuleCreationInstruction.ROTATE_THRICE);
        defaultConfigurations.add(basicConfig);
        return defaultConfigurations;
    }

    private static void changeProbabilityForConfig(ConfigurationHolder config, String filename, int probability) {
        config.probabilityChange().put(filename,probability);
    }
}
