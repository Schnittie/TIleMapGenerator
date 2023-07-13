package de.schnittie.model.database.configurations;

import de.schnittie.model.database.InstallationHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;

public class CreatingConfigurationService {
    public static void createNewConfiguration(String tileFolder, HashMap<InputStream, RuleCreationInstruction> tilemapToRotationInstructionMap, HashMap<InputStream, String> inputStreamToFilenameMap) {
        String pathToConfigFolderString = InstallationHandler.getResourcesURLandIfNotExistsCreate() + File.separator + tileFolder;
        Path pathForFolder = Path.of(pathToConfigFolderString);
        if (Files.exists(pathForFolder)) {
            System.out.println("Failed to create Configuration: config Folder of that name already exists");
            return;
        }
        HashMap<String, Integer> emptyInstructionsMap = new HashMap<>(0);
        try {
            InstallationHandler.createAndFillDirectoryWithDefaultImageAndDB(tileFolder);
            InstallationHandler.generateTilesIfNotPresent(new ConfigurationHolder(tilemapToRotationInstructionMap,
                    tileFolder, emptyInstructionsMap, inputStreamToFilenameMap));
        } catch (IOException | URISyntaxException e) {
            System.out.println("Failed to create directory " + tileFolder + " at " + pathToConfigFolderString);
            return;
        } catch (SQLException e) {
            System.out.println("Failed to generate Rules for new Config " + tileFolder);
        }

    }
}
