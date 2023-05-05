import java.util.ArrayList;

public class RulesMountain implements RuleList {
    ArrayList<Rule> rules = new ArrayList<>();

    private RulesMountain(){
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.BELOW));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.LEFT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.DOWNLEFT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.DOWNRIGHT));

        rules.add(new Rule(RuleList.HILLID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.HILLID, EDirection.BELOW));
        rules.add(new Rule(RuleList.HILLID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.HILLID, EDirection.LEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.HILLID, EDirection.DOWNLEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.DOWNRIGHT));
    }
    private static final RulesMountain rulesMountain = new RulesMountain();
    public static RulesMountain getInstance(){
        return rulesMountain;
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
