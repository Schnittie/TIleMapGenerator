package de.schnittie.model.database.configurations;

import java.io.File;
import java.util.HashMap;

public record ConfigurationHolder(
        HashMap<File, RuleCreationInstruction> fileToRotateInstructionMap, String nameOfConfiguration,
        HashMap<Integer, Integer> probabilityChange) {
}
