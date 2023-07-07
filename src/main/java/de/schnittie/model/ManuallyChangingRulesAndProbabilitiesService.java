package de.schnittie.model;

import de.schnittie.model.database.DBinteractions;
import de.schnittie.model.database.RuleTO;
import de.schnittie.model.database.fillingDB.InvalidAdjacencyException;
import de.schnittie.model.database.fillingDB.RuleValidationService;

public class ManuallyChangingRulesAndProbabilitiesService {
    private static DBinteractions dBinteractions = DBinteractions.getInstance();

    public static void removeRule(int ruleID){
        RuleTO ruleToRemove = dBinteractions.getRuleById(ruleID);
        dBinteractions.removeRule(ruleID);
        try {
            RuleValidationService.areRulesValid(dBinteractions.getAllRules());
        } catch (InvalidAdjacencyException e) {
            System.out.println("Failed attempt to alter Rules : Rules aren't valid");
        dBinteractions.putRuleIntoDB(ruleToRemove);
        }
    }
    public static void addRule(int this_tile_id,int that_tile_id, int direction){
        dBinteractions.putRuleIntoDB(new RuleTO(-1, this_tile_id, that_tile_id, direction));
        dBinteractions.putRuleIntoDB(new RuleTO(-1, that_tile_id, this_tile_id, dBinteractions.getReverseDirection().get(direction)));
    }
    public static void changeProbability(int ruleID, int probability){
        dBinteractions.setProbabilityForTile(ruleID,probability);
    }
}
