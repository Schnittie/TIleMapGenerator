package de.schnittie.model;

import de.schnittie.model.logic.Configuration;
import de.schnittie.model.database.DBinteractions;
import de.schnittie.model.database.RuleTO;
import de.schnittie.model.database.fillingDB.InvalidAdjacencyException;
import de.schnittie.model.database.fillingDB.RuleValidationUtility;

import java.util.HashMap;

public class ManuallyChangingRulesAndProbabilitiesUtility {
    private static DBinteractions dBinteractions = DBinteractions.getInstance();

    public static void removeRule(int ruleID) {
        RuleTO ruleToRemove = dBinteractions.getRuleById(ruleID);
        dBinteractions.removeRule(ruleID);
        try {
            RuleValidationUtility.areRulesValid(dBinteractions.getAllRules());
        } catch (InvalidAdjacencyException e) {
            System.out.println("Failed attempt to alter Rules : Rules aren't valid");
            dBinteractions.putRuleIntoDB(ruleToRemove);
        }
    }


    private static void changeProbability(String filename, int probability) {
        HashMap<String, Integer> filenameToIDMap = Configuration.getInstance().getFilenameToIDMap();
        dBinteractions.setProbabilityForTile(filenameToIDMap.get(filename), probability);
    }

    private static void changeProbability(int ruleID, int probability) {
        dBinteractions.setProbabilityForTile(ruleID, probability);
    }

    public static void changeProbabilityOfFileName(HashMap<String, Integer> probabilityMap) {
        for (String filename : probabilityMap.keySet()) {
            changeProbability(filename, probabilityMap.get(filename));
        }
        ConfigurationLoaderUtility.reloadConfiguration();
    }
    public static void changeProbability(HashMap<Integer, Integer> probabilityMap) {
        for (Integer id : probabilityMap.keySet()) {
            changeProbability(id, probabilityMap.get(id));
        }
        ConfigurationLoaderUtility.reloadConfiguration();
    }

}
