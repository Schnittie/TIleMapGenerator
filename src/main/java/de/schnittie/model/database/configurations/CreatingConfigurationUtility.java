package de.schnittie.model.database.configurations;

import de.schnittie.model.database.InstallationHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreatingConfigurationUtility {
    private ConfigurationHolder newConfig;
    private Map<String, RuleCreationInstruction> pathToInstructionMap = new HashMap<>();

    public CreatingConfigurationUtility(String configName) {
        this.newConfig = new ConfigurationHolder(new HashMap<>(), configName, new HashMap<>(), new HashMap<>());
    }

    public void addTileMapToConfig(String filename, RuleCreationInstruction ruleCreationInstruction) {
        pathToInstructionMap.put(filename, ruleCreationInstruction);
    }

    public void finishNewConfig() {
        String pathToConfigFolderString = InstallationHandler.getResourcesURLandIfNotExistsCreate() + File.separator + newConfig.nameOfConfiguration();
        Path pathForFolder = Path.of(pathToConfigFolderString);
        if (Files.exists(pathForFolder)) {
            System.out.println("Failed to create Configuration: config Folder of that name already exists");
            return;
        }
        try {
            for (String filename : pathToInstructionMap.keySet()) {
                Path filePath = Paths.get(filename);
                InputStream inputStream = Files.newInputStream(filePath);
                newConfig.inputStreamToFilenameMap().put(inputStream, filePath.toFile().getName());
                newConfig.inputStreamToRotateInstructionMap().put(inputStream, pathToInstructionMap.get(filename));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InstallationHandler.createAndFillDirectoryWithDefaultImageAndDB(newConfig.nameOfConfiguration());
            InstallationHandler.generateTilesIfNotPresent(newConfig);
        } catch (IOException | URISyntaxException e) {
            System.out.println("Failed to create directory " + newConfig.nameOfConfiguration() + " at " + pathToConfigFolderString);
            return;
        } catch (SQLException e) {
            System.out.println("Failed to generate Rules for new Config " + newConfig.nameOfConfiguration());
        }

    }
}
