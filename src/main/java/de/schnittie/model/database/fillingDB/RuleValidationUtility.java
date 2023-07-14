package de.schnittie.model.database.fillingDB;

import de.schnittie.model.database.RuleTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RuleValidationUtility {
    public static void areRulesValid(List<RuleTO> rules) throws InvalidAdjacencyException {
        if (rules.isEmpty()){
            return;
        }
        HashMap<Integer, HashSet<Integer>> tileIdToPresentDirectionIDs = new HashMap<>(rules.size());
        for (RuleTO rule : rules) {
            if (!tileIdToPresentDirectionIDs.containsKey(rule.this_tile())){
                tileIdToPresentDirectionIDs.put(rule.this_tile(), new HashSet<>(4));
            }
            tileIdToPresentDirectionIDs.get(rule.this_tile()).add(rule.next_to());
        }
        int goalDirectionAmount = tileIdToPresentDirectionIDs.get(rules.get(0).this_tile()).size();
        for (Integer tileID : tileIdToPresentDirectionIDs.keySet()) {
            if (goalDirectionAmount != tileIdToPresentDirectionIDs.get(tileID).size()){
                throw new InvalidAdjacencyException(tileIdToPresentDirectionIDs);
            }
        }
    }

}

