import java.util.ArrayList;
public interface RuleList {
    ArrayList<Rule> rules = new ArrayList<>();
    int LANDID = 0;
     int COASTID = 1;
     int SEAID = 2;
     int HILLID = 3;
     int REEFID = 4;
     int MOUNTAINID = 5;
     int OCEANID = 6;
     default boolean canThisBeHere(EDirection direction, ETileContent tileContent) {
        for (Rule rule : rules) {
            if (rule.equals(new Rule(tileContent.getId(), direction))) {
                return true;
            }
        }
        return false;
    }

     default boolean canThisBeHere(EDirection direction, ArrayList<ETileContent> listOfPossibilitiesNow) {
         for (Rule rule : this.rules) {
             for (ETileContent tileContent:listOfPossibilitiesNow) {
                 if (rule.equals(new Rule(tileContent.getId(), direction))) {
                     return true;
                 }
             }
         }
         return false;
    }
}
