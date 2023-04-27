import java.util.ArrayList;

public class CoastRules extends RuleList {
    private ArrayList<Rule> rules = new ArrayList<>();
    
    public CoastRules() {
        rules.add(new Rule(LANDID, Direction.ABOVE));
        rules.add(new Rule(LANDID, Direction.TORIGHT));
        rules.add(new Rule(LANDID, Direction.BELOW));

        rules.add(new Rule(COASTID, Direction.ABOVE));
        rules.add(new Rule(COASTID, Direction.BELOW));
        rules.add(new Rule(COASTID, Direction.TORIGHT));
        rules.add(new Rule(COASTID, Direction.TOLEFT));

        rules.add(new Rule(SEAID, Direction.ABOVE));
        rules.add(new Rule(SEAID, Direction.BELOW));
        rules.add(new Rule(SEAID, Direction.TOLEFT));
    }

    public boolean canThisBeHere(Direction direction, TileContent tileContent) {
        for (Rule rule : rules) {
            if (rule.equals(new Rule(tileContent.getId(), direction))) {
                return true;
            }
        }
        return false;
    }
}
