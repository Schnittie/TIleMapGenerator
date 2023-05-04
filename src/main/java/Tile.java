import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Getter

public class Tile {
    private int possibleTileStatesLeft = ETileContent.values().length;
    private final boolean[] canIbe;
    private boolean isCollapsed = false;
    private ETileContent content;

    public Tile() {
        canIbe = new boolean[possibleTileStatesLeft];
        Arrays.fill(canIbe, true);
    }

    public ArrayList<ETileContent> getPossibleTileContentLeft() {
        ArrayList<ETileContent> response = new ArrayList<>();
        if (isCollapsed) {
            response.add(content);
            return response;
        }
        for (int i = 0; i < canIbe.length; i++) {
            if (canIbe[i]) {
                response.add(ETileContent.findById(i));
            }
        }
        return response;
    }

    public boolean propagate(EDirection whereIamRelativeToCaller, ETileContent whatItIsNow) {
        if (isCollapsed) return false;
        for (int i = 0; i < ETileContent.values().length; i++) {
            if (canIbe[i]) {
                if (!ETileContent.findById(i).getRuleList().canThisBeHere(whereIamRelativeToCaller, whatItIsNow)) {
                    if (removePossibility(i)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public PropagationResponseEntity propagate(EDirection whereIamRelativeToCaller, ArrayList<ETileContent> listOfPossbilitiesNow) {
        PropagationResponseEntity response = new PropagationResponseEntity(false, false, new ArrayList<>());
        if (isCollapsed) return response;
        for (int i = 0; i < ETileContent.values().length; i++) {
            if (canIbe[i]) {
                if (!ETileContent.findById(i).getRuleList().canThisBeHere(whereIamRelativeToCaller, listOfPossbilitiesNow)) {
                    response.setHasChangedPossibility(true);
                    if (removePossibility(i)) {
                        return new PropagationResponseEntity(true, false, null);
                    }
                } else {
                    ArrayList<ETileContent> listOfPossibilities = response.getNewPossibilities();
                    listOfPossibilities.add(ETileContent.findById(i));
                    response.setNewPossibilities(listOfPossibilities);
                }
            }
        }
        return response;
    }

    private boolean removePossibility(int toRemove) {
        canIbe[toRemove] = false;
        possibleTileStatesLeft--;
        if (possibleTileStatesLeft != 1) {
            return false;
        }
        return true;
    }

    public void collapse(int i) {
        if (!isCollapsed) {
            isCollapsed = true;
            content = ETileContent.findById(i);
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
