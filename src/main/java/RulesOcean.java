import java.util.ArrayList;

public class RulesOcean extends RuleList {
    private ArrayList<Rule> rules = new ArrayList<>();

    public RulesOcean(){
        rules.add(new Rule(RuleList.SEAID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.SEAID, EDirection.BELOW));
        rules.add(new Rule(RuleList.SEAID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.DOWNRIGHT));

        rules.add(new Rule(RuleList.REEFID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.REEFID, EDirection.BELOW));
        rules.add(new Rule(RuleList.REEFID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.REEFID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.REEFID, EDirection.DOWNRIGHT));

        rules.add(new Rule(RuleList.OCEANID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.OCEANID, EDirection.BELOW));
        rules.add(new Rule(RuleList.OCEANID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.LEFT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.DOWNLEFT));

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
