package de.schnittie.model.database;

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
import java.util.HashMap;
import java.util.Objects;

public class InstallationHandler {
    private final static AppDirs appDirs = AppDirsFactory.getInstance();
    private final static String DEFAULT_FOLDER_NAME = "defaultMapConfig";
    private final static String pathString = appDirs.getUserDataDir("TileMapGenerator", null, "CatboyMaps");
    private static final String defaultMapPathString = pathString + File.separator + DEFAULT_FOLDER_NAME;
    private static final String tileFolder = defaultMapPathString + File.separator + "TileImages";

    public static Path getDefaultResourcesURLandIfNotExistsCreate() {
        Path path = Path.of(defaultMapPathString);
        if (Files.exists(path)) {
            return path;
        }
        try {
            //creating the Directory
            Files.createDirectories(Path.of(pathString));
            Files.createDirectories(path);
            Files.createDirectories(Path.of(tileFolder));
            System.out.println("Directory created successfully");
        } catch (IOException e) {
            System.out.println("failed to create directory");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            //copying over Resources
            ClassLoader classLoader = InstallationHandler.class.getClassLoader();

            copyFileFromResourcesToDefaultDirectory(classLoader, "default.png");
            copyFileFromResourcesToDefaultDirectory(classLoader, "TileMapGeneratorDefaultDB.db");

            System.out.println("successfully copied resources");
        } catch (IOException | URISyntaxException e) {
            System.out.println("failed to copy resources");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return path;
    }

    public static void generateRulesAndTilesForDefaultMapIfNotPresent() throws URISyntaxException {
        if (!DBinteractions.getInstance().getPossibleTileIDs().isEmpty()) {
            return;
        }
        ClassLoader classLoader = InstallationHandler.class.getClassLoader();
        HashMap<File, Integer> tilemapMap = new HashMap<>(3);
        tilemapMap.put(new File(Objects.requireNonNull(Objects.requireNonNull(classLoader.getResource(
                "DefaultTilemapRotateNone.png")).toURI())), 0);
        tilemapMap.put(new File(Objects.requireNonNull(Objects.requireNonNull(classLoader.getResource(
                "DefaultTilemapRotateOnce.png")).toURI())), 1);
        tilemapMap.put(new File(Objects.requireNonNull(Objects.requireNonNull(classLoader.getResource(
                "DefaultTilemapRotateThrice.png")).toURI())), 3);
        TileCreation.addTiles(tilemapMap, tileFolder + File.separator);
    }

    private static void copyFileFromResourcesToDefaultDirectory(ClassLoader classLoader, String filename) throws URISyntaxException, IOException {
        URI absoluteSourcePath = Objects.requireNonNull(classLoader.getResource(filename)).toURI();
        Files.copy(Path.of(absoluteSourcePath), Path.of(
                defaultMapPathString + File.separator + filename), StandardCopyOption.REPLACE_EXISTING);
    }


}
