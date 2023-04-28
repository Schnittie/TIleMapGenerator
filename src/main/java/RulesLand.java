import java.util.ArrayList;

public class RulesLand extends RuleList {
    private  ArrayList<Rule> rules = new ArrayList<>();
    public RulesLand(){
        rules.add(new Rule(RuleList.LANDID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.LANDID, EDirection.BELOW));
        rules.add(new Rule(RuleList.LANDID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.LEFT));
        rules.add(new Rule(RuleList.LANDID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.LANDID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.COASTID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.COASTID, EDirection.BELOW));
        rules.add(new Rule(RuleList.COASTID, EDirection.LEFT));
        rules.add(new Rule(RuleList.COASTID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.COASTID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.COASTID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.COASTID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.HILLID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.HILLID, EDirection.BELOW));
        rules.add(new Rule(RuleList.HILLID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.HILLID, EDirection.LEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.HILLID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.HILLID, EDirection.DOWNLEFT));

    }
    public boolean canThisBeHere(EDirection direction, ETileContent tileContent) {
        for (Rule rule : rules) {
            if (rule.equals(new Rule(tileContent.getId(),direction))){
                return true;
            }
        }
        return false;
    }
}
