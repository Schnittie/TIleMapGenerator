package de.schnittie.model.database.configurations;

import java.io.InputStream;
import java.util.HashMap;

public record ConfigurationHolder(
        HashMap<InputStream, RuleCreationInstruction> inputStreamToRotateInstructionMap, String nameOfConfiguration,
        HashMap<String, Integer> probabilityChange, HashMap<InputStream, String> inputStreamToFilenameMap) {
}
