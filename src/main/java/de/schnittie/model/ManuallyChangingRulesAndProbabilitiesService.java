package de.schnittie.model;

import de.schnittie.model.database.DBinteractions;
import de.schnittie.model.database.RuleTO;
import de.schnittie.model.database.fillingDB.InvalidAdjacencyException;
import de.schnittie.model.database.fillingDB.RuleValidationService;

public class ManuallyChangingRulesAndProbabilitiesService {
    public static DBinteractions dBinteractions = DBinteractions.getInstance();

    public void removeRule(int ruleID){
        RuleTO ruleToRemove = dBinteractions.getRuleById(ruleID);
        dBinteractions.removeRule(ruleID);
        try {
            RuleValidationService.areRulesValid(dBinteractions.getAllRules());
        } catch (InvalidAdjacencyException e) {
            System.out.println("Failed attempt to alter Rules : Rules aren't valid");
        dBinteractions.putRulesIntoDB(ruleToRemove);
        }
    }
    public void addRule(int this_tile_id,int that_tile_id, int direction){
        dBinteractions.putRulesIntoDB(new RuleTO(-1, this_tile_id, that_tile_id, direction));
        dBinteractions.putRulesIntoDB(new RuleTO(-1, that_tile_id, this_tile_id, dBinteractions.getReverseDirection().get(direction)));
    }
    public void changeProbability(int ruleID, int probability){
        dBinteractions.setProbabilityForTile(ruleID,probability);
    }
}
