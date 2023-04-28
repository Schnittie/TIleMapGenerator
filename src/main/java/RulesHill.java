import java.util.ArrayList;

public class RulesHill implements RuleList {
    ArrayList<Rule> rules = new ArrayList<>();

    public RulesHill(){
        rules.add(new Rule(RuleList.HILLID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.HILLID, EDirection.BELOW));
        rules.add(new Rule(RuleList.HILLID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.HILLID, EDirection.LEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.HILLID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.HILLID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.LANDID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.LANDID, EDirection.BELOW));
        rules.add(new Rule(RuleList.LANDID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.LEFT));
        rules.add(new Rule(RuleList.LANDID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.LANDID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.BELOW));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.DOWNRIGHT));

    }
    public boolean canThisBeHere(EDirection direction, ArrayList<ETileContent> listOfPossbilitiesNow) {
        for (Rule rule : this.rules) {
            for (ETileContent tileContent:listOfPossbilitiesNow) {
                if (rule.equals(new Rule(tileContent.getId(), direction))) {
                    return true;
                }
            }
        }
        return false;
    }
}
