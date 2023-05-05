import java.util.ArrayList;

public class RulesReef implements RuleList {
    ArrayList<Rule> rules = new ArrayList<>();

    private RulesReef(){
        rules.add(new Rule(RuleList.SEAID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.SEAID, EDirection.BELOW));
        rules.add(new Rule(RuleList.SEAID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.LEFT));
        rules.add(new Rule(RuleList.SEAID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.SEAID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.REEFID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.REEFID, EDirection.BELOW));
        rules.add(new Rule(RuleList.REEFID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.REEFID, EDirection.LEFT));
        rules.add(new Rule(RuleList.REEFID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.REEFID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.REEFID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.REEFID, EDirection.DOWNLEFT));
    }

    private static RulesReef rulesReef = new RulesReef();
    public static RulesReef getInstance(){
        return rulesReef;
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
