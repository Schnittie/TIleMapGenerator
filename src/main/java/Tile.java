import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Getter

public class Tile {
    private int possibleTileStatesLeft = TileContent.values().length;
    private boolean[] canIbe;
    private boolean isCollapsed = false;
    private TileContent content;

    public Tile() {
        canIbe = new boolean[possibleTileStatesLeft];
        Arrays.fill(canIbe, true);
    }

    public boolean propagate(Direction whereIamRelativeToCaller, TileContent whatItIsNow) {
        if (isCollapsed) return false;
        for (int i = 0; i < TileContent.values().length; i++) {
            if (canIbe[i]) {
                if (!TileContent.findById(i).getRuleList().canThisBeHere(whereIamRelativeToCaller, whatItIsNow)) {
                    if (removePossibility(i)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean removePossibility(int toRemove) {
        canIbe[toRemove] = false;
        possibleTileStatesLeft--;
        if (possibleTileStatesLeft != 1) {
            return false;
        }
        for (int i = 0; i < canIbe.length; i++) {
            if (canIbe[i]) {
                return true;
            }
        }
        //if it gets to here there is an error
        return false;

    }

    public void collapse(int i) {
        if (!isCollapsed) {
            isCollapsed = true;
            content = TileContent.findById(i);
        }
    }

    public void collapse() {
        ArrayList<Integer> possibleStates = new ArrayList<>(3);
        for (int i = 0; i < canIbe.length; i++) {
            if (canIbe[i]) {
                possibleStates.add(i);
            }
        }
        Random random = new Random();
        collapse(possibleStates.get(random.nextInt(possibleStates.size())));
    }
}
