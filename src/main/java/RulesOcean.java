import java.util.ArrayList;

public class RulesOcean implements RuleList {
    ArrayList<Rule> rules = new ArrayList<>();

    private RulesOcean(){
        rules.add(new Rule(RuleList.OCEANID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.OCEANID, EDirection.BELOW));
        rules.add(new Rule(RuleList.OCEANID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.LEFT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.DOWNRIGHT));
        rules.add(new Rule(RuleList.OCEANID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.SEAID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.SEAID, EDirection.BELOW));
        rules.add(new Rule(RuleList.SEAID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.SEAID, EDirection.DOWNRIGHT));
    }

    private static final RulesOcean rulesOcean = new RulesOcean();
    public static RulesOcean getInstance(){
        return rulesOcean;
    }
    public boolean canThisBeHere(EDirection direction, ArrayList<ETileContent> listOfPossibilitiesNow) {
        for (Rule rule : this.rules) {
            for (ETileContent tileContent: listOfPossibilitiesNow) {
                if (rule.equals(new Rule(tileContent.getId(), direction))) {
                    return true;
                }
            }
        }
        return false;
    }
}
