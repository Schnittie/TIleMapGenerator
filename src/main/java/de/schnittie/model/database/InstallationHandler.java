package de.schnittie.model.database;

import de.schnittie.model.ManuallyChangingRulesAndProbabilitiesService;
import de.schnittie.model.database.configurations.ConfigurationHolder;
import de.schnittie.model.database.configurations.DefaultConfigurationService;
import de.schnittie.model.database.fillingDB.TileCreation;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class InstallationHandler {
    private final static AppDirs appDirs = AppDirsFactory.getInstance();
    private final static String pathString = appDirs.getUserDataDir("TileMapGenerator", null, "CatboyMaps");
    private static final String TILE_FOLDER_NAME = "TileImages";
    private static final ArrayList<ConfigurationHolder> defaultConfigs;

    static {
        try {
            defaultConfigs = DefaultConfigurationService.getDefaultConfigurations();
        } catch (URISyntaxException e) {
            System.out.println("Failed to load default configuration files");
            throw new RuntimeException(e);
        }
    }

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
            for (ConfigurationHolder defaultConfig : DefaultConfigurationService.getDefaultConfigurations()) {

                createAndFillDirectoryWithDefaultImageAndDB(defaultConfig.nameOfConfiguration());
            }

            System.out.println("Directory created and Resources copied successfully");
        } catch (IOException | URISyntaxException e) {
            System.out.println("failed to create directory or copy Resources");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return path;
    }

    public static void createAndFillDirectoryWithDefaultImageAndDB(String folderName) throws IOException, URISyntaxException {
        String pathStringToDirectory = pathString + File.separator + folderName;

        //creating the Directory
        Files.createDirectories(Path.of(pathStringToDirectory));
        Files.createDirectories(Path.of(pathStringToDirectory + File.separator + TILE_FOLDER_NAME));

        //copying over Resources
        copyFileFromResourcesToDirectory("default.png", pathStringToDirectory);
        copyFileFromResourcesToDirectory("damage.png", pathStringToDirectory);
        copyFileFromResourcesToDirectory("TileMapGeneratorDB.db", pathStringToDirectory);
    }

    public static void generateTilesForDefaultMapIfNotPresent() {
        for (ConfigurationHolder defaultConfig : defaultConfigs) {
            try {
                generateTilesIfNotPresent(defaultConfig);
            } catch (URISyntaxException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void generateTilesIfNotPresent(ConfigurationHolder config) throws URISyntaxException, SQLException {
        String pathStringToDirectory = pathString + File.separator + config.nameOfConfiguration();
        if (!DBinteractions.getInstance().setDbFolder(pathStringToDirectory).getPossibleTileIDs().isEmpty()) {
            return;
        }
        System.out.println("Resources for " + config.nameOfConfiguration() + " successfully copied");
        TileCreation.addTiles(config.inputStreamToRotateInstructionMap(), pathStringToDirectory +
                File.separator + TILE_FOLDER_NAME + File.separator, config.inputStreamToFilenameMap());
        System.out.println("Applying custom Rule changes...");
        ManuallyChangingRulesAndProbabilitiesService.changeProbabilityOfFileName(config.probabilityChange());

        System.out.println("Rules for " + config.nameOfConfiguration() + " successfully Generated");

    }

    private static void copyFileFromResourcesToDirectory(String filename, String pathStringToDirectory) throws URISyntaxException, IOException {
        ClassLoader classLoader = InstallationHandler.class.getClassLoader();
        InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(filename));
        Files.copy(inputStream, Path.of(pathStringToDirectory + File.separator + filename), StandardCopyOption.REPLACE_EXISTING);
        inputStream.close();
    }


}
