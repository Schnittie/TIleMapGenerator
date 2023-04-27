import java.util.ArrayList;

public class HillRules extends RuleList {
    private ArrayList<Rule> rules = new ArrayList<>();

    public HillRules(){
        rules.add(new Rule(HILLID, Direction.ABOVE));
        rules.add(new Rule(HILLID, Direction.BELOW));
        rules.add(new Rule(HILLID, Direction.TORIGHT));
        rules.add(new Rule(HILLID, Direction.TOLEFT));

        rules.add(new Rule(LANDID, Direction.ABOVE));
        rules.add(new Rule(LANDID, Direction.BELOW));
        rules.add(new Rule(LANDID, Direction.TORIGHT));
        rules.add(new Rule(LANDID, Direction.TOLEFT));

        rules.add(new Rule(MOUNTAINID, Direction.ABOVE));
        rules.add(new Rule(MOUNTAINID, Direction.BELOW));
        rules.add(new Rule(MOUNTAINID, Direction.TORIGHT));

    }
    public boolean canThisBeHere(Direction direction, TileContent tileContent) {
        for (Rule rule : rules) {
            if (rule.equals(new Rule(tileContent.getId(),direction))){
                return true;
            }
        }
        return false;
    }
}
