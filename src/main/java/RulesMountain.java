import java.util.ArrayList;

public class RulesMountain implements RuleList {
    ArrayList<Rule> rules = new ArrayList<>();

    public RulesMountain(){

        rules.add(new Rule(RuleList.HILLID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.HILLID, EDirection.BELOW));
        rules.add(new Rule(RuleList.HILLID, EDirection.LEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.HILLID, EDirection.DOWNLEFT));

        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.ABOVE));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.BELOW));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.RIGHT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.LEFT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.TOPLEFT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.TOPRIGHT));
        rules.add(new Rule(RuleList.MOUNTAINID, EDirection.DOWNLEFT));
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
