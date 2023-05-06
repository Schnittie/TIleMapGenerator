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

    public PropagationResponseEntity propagate(EDirection whereIamRelativeToCaller, ArrayList<ETileContent> listOfPossibilitiesNow) {
        PropagationResponseEntity response = new PropagationResponseEntity(false, false, new ArrayList<>());
        if (isCollapsed) return response;
        for (int i = 0; i < ETileContent.values().length; i++) {
            if (canIbe[i]) {
                if (!ETileContent.findById(i).getRuleList().canThisBeHere(whereIamRelativeToCaller, listOfPossibilitiesNow)) {
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

    public void collapse() throws MapGenerationException {
        if (isCollapsed) throw new MapGenerationException();

        ArrayList<ETileContent> possibleStates = new ArrayList<>(ETileContent.values().length);
        for (int i = 0; i < canIbe.length; i++) {
            if (canIbe[i]) {
                possibleStates.add(ETileContent.findById(i));
            }
        }
        collapse(getRandomState(possibleStates));
    }

    private int getRandomState(ArrayList<ETileContent> possibleStates) throws MapGenerationException {
        if(possibleStates.isEmpty()){
            throw new RuntimeException("No Possible State");
        }
        int totalProbability = 0;
        for (ETileContent tileContent : possibleStates) {
            totalProbability += tileContent.getProbability();
        }
        Random random = new Random();
        if (totalProbability <= 0) {
            return possibleStates.get(random.nextInt(possibleStates.size())).getId();
        }
        int randomInt = random.nextInt(totalProbability);
        for (ETileContent tileContent : possibleStates) {
            randomInt -= tileContent.getProbability();
            if (randomInt < 0) {
                return tileContent.getId();
            }
        }
        throw new MapGenerationException();
    }
}
