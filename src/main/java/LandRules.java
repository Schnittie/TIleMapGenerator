import java.util.ArrayList;

public class LandRules extends RuleList {
    private  ArrayList<Rule> rules = new ArrayList<>();
    public LandRules(){
        rules.add(new Rule(LANDID, Direction.ABOVE));
        rules.add(new Rule(LANDID, Direction.BELOW));
        rules.add(new Rule(LANDID, Direction.TOLEFT));
        rules.add(new Rule(LANDID, Direction.TORIGHT));

        rules.add(new Rule(COASTID, Direction.ABOVE));
        rules.add(new Rule(COASTID, Direction.BELOW));
        rules.add(new Rule(COASTID, Direction.TOLEFT));

        rules.add(new Rule(HILLID, Direction.ABOVE));
        rules.add(new Rule(HILLID, Direction.BELOW));
        rules.add(new Rule(HILLID, Direction.TORIGHT));
        rules.add(new Rule(HILLID, Direction.TOLEFT));

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
