import java.util.ArrayList;

public class RulesCoast implements RuleList {
    ArrayList<Rule> rules = new ArrayList<>();
    public RulesCoast() {
        rules.add(new Rule(RuleList.LANDID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.LANDID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.BELOW));
        rules.add(new Rule(RuleList.LANDID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.LANDID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.LANDID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.COASTID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.COASTID, EDirection.BELOW));
        rules.add(new Rule(RuleList.COASTID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.COASTID, EDirection.LEFT));
        rules.add(new Rule(RuleList.COASTID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.COASTID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.COASTID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.COASTID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.SEAID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.SEAID, EDirection.BELOW));
        rules.add(new Rule(RuleList.SEAID, EDirection.LEFT));
        rules.add(new Rule(RuleList.SEAID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.SEAID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.DOWNLEFT));
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
