package de.schnittie.model.database;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class InstallationHandler {
    private final static AppDirs appDirs = AppDirsFactory.getInstance();

    public static Path getResourcesURLandIfNotExistsCreate() {
        String pathString = appDirs.getUserDataDir("LegoBattlesMapGenerator", null, "Laurenz");
        Path path = Path.of(pathString);
        if (Files.exists(path)) {
            return path;
        }
        try {
            Files.createDirectories(path);
            Files.createDirectories(Path.of(pathString + File.separator + "TileImages"));
            System.out.println("Directory created");
        } catch (IOException e) {
            System.out.println("failed to create directory");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            ClassLoader classLoader = InstallationHandler.class.getClassLoader();
            URI absoluteSourcePath = classLoader.getResource("default.png").toURI();
            Files.copy(Path.of(absoluteSourcePath), Path.of(pathString + File.separator + "default.png"), StandardCopyOption.REPLACE_EXISTING);
            absoluteSourcePath = classLoader.getResource("LegoBattlesMapGeneratorDB.db").toURI();
            Files.copy(Path.of(absoluteSourcePath), Path.of(pathString + File.separator + "LegoBattlesMapGeneratorDB.db"), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("successfully copied resources");
        } catch (IOException | URISyntaxException e) {
            System.out.println("failed to copy resources");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return path;
    }


}
