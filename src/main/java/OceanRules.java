import java.util.ArrayList;

public class OceanRules extends RuleList {
    private ArrayList<Rule> rules = new ArrayList<>();

    public OceanRules(){
        rules.add(new Rule(SEAID, Direction.ABOVE));
        rules.add(new Rule(SEAID, Direction.BELOW));
        rules.add(new Rule(SEAID, Direction.TORIGHT));

        rules.add(new Rule(REEFID, Direction.ABOVE));
        rules.add(new Rule(REEFID, Direction.BELOW));
        rules.add(new Rule(REEFID, Direction.TORIGHT));

        rules.add(new Rule(OCEANID, Direction.ABOVE));
        rules.add(new Rule(OCEANID, Direction.BELOW));
        rules.add(new Rule(OCEANID, Direction.TORIGHT));
        rules.add(new Rule(OCEANID, Direction.TOLEFT));

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
