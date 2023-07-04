package de.schnittie.model.database;

import de.schnittie.model.ManuallyChangingRulesAndProbabilitiesService;
import de.schnittie.model.database.defaultConfigurations.DefaultConfigurationHolder;
import de.schnittie.model.database.defaultConfigurations.DefaultConfigurationService;
import de.schnittie.model.database.fillingDB.TileCreation;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class InstallationHandler {
    private final static AppDirs appDirs = AppDirsFactory.getInstance();
    private final static String pathString = appDirs.getUserDataDir("TileMapGenerator", null, "CatboyMaps");
    private static final String TILE_FOLDER_NAME = "TileImages";
    private static final HashSet<DefaultConfigurationHolder> defaultConfigs = DefaultConfigurationService.getDefaultConfigurations();

    public static String getTileFolderName() {
        return TILE_FOLDER_NAME;
    }

    public static Path getResourcesURLandIfNotExistsCreate() {
        Path path = Path.of(pathString);
        if (Files.exists(path)) {
            return path;
        }
        System.out.println("Creating Resources at " + pathString);
        try {
            Files.createDirectories(path);
            for (DefaultConfigurationHolder defaultConfig : defaultConfigs) {
                //creating the Directory
                String pathStringToDirectory = pathString + File.separator + defaultConfig.nameOfConfiguration();
                Files.createDirectories(Path.of(pathStringToDirectory));
                Files.createDirectories(Path.of(pathStringToDirectory + File.separator + TILE_FOLDER_NAME));

                //copying over Resources
                copyFileFromResourcesToDefaultDirectories("default.png", pathStringToDirectory);
                copyFileFromResourcesToDefaultDirectories("TileMapGeneratorDB.db", pathStringToDirectory);
            }

            System.out.println("Directory created and Resources copied successfully");
        } catch (IOException | URISyntaxException e) {
            System.out.println("failed to create directory or copy Resources");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return path;
    }

    public static void generateTilesForDefaultMapIfNotPresent() {
        for (DefaultConfigurationHolder defaultConfig : defaultConfigs) {
            try {
                generateTilesForOneDefaultMapIfNotPresent(defaultConfig);
            } catch (URISyntaxException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void generateTilesForOneDefaultMapIfNotPresent(DefaultConfigurationHolder defaultConfig) throws URISyntaxException, SQLException {
        String pathStringToDirectory = pathString + File.separator + defaultConfig.nameOfConfiguration();
        if (!DBinteractions.getInstance().setDbFolder(pathStringToDirectory).getPossibleTileIDs().isEmpty()) {
            return;
        }

        ClassLoader classLoader = InstallationHandler.class.getClassLoader();
        HashMap<File, Integer> tilemapMap = new HashMap<>(defaultConfig.filepathToRotateInstructionMap().size());

        for (String fileName : defaultConfig.filepathToRotateInstructionMap().keySet()) {
            tilemapMap.put(new File(Objects.requireNonNull(Objects.requireNonNull(classLoader.getResource(
                    fileName)).toURI())), defaultConfig.filepathToRotateInstructionMap().get(fileName));
        }

        System.out.println("Resources for " + defaultConfig.nameOfConfiguration() + " successfully copied");
        TileCreation.addTiles(tilemapMap, pathStringToDirectory + File.separator + TILE_FOLDER_NAME + File.separator);
        System.out.println("Applying custom Rule changes...");

        for (Integer probabilityChangeId : defaultConfig.probabilityChange().keySet()) {
            ManuallyChangingRulesAndProbabilitiesService.changeProbability(probabilityChangeId,
                    defaultConfig.probabilityChange().get(probabilityChangeId));
        }

        System.out.println("Rules for " + defaultConfig.nameOfConfiguration() + " successfully Generated");

    }

    private static void copyFileFromResourcesToDefaultDirectories(String filename, String pathStringToDirectory) throws URISyntaxException, IOException {
        ClassLoader classLoader = InstallationHandler.class.getClassLoader();
        URI absoluteSourcePath = Objects.requireNonNull(classLoader.getResource(filename)).toURI();
        Files.copy(Path.of(absoluteSourcePath), Path.of(
                pathStringToDirectory + File.separator + filename), StandardCopyOption.REPLACE_EXISTING);
    }


}
