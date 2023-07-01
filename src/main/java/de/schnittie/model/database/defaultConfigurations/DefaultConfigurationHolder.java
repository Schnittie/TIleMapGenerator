package de.schnittie.model.database.defaultConfigurations;

import java.util.HashMap;

public record DefaultConfigurationHolder(HashMap<String , Integer> filepathToRotateInstructionMap, String nameOfConfiguration) {
}
