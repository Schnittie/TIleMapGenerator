package businesscode;

import database.DBinteractions;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter

public class Tile {
    private int possibleTileStatesLeft;
    private HashMap<Integer, Boolean> canIbe;
    private boolean isCollapsed = false;
    private int content = -1;
    private DBinteractions dBinteractions = DBinteractions.getInstance();

    public Tile(int possibleTileStatesLeft, int[] possibleTileIDs) {
        this.possibleTileStatesLeft = possibleTileStatesLeft;
        canIbe = new HashMap<>(possibleTileStatesLeft);
        for (int possibleTileID : possibleTileIDs) {
            canIbe.put(possibleTileID, true);
        }
    }

    public List<Integer> getPossibleTileContentLeft() {
        List<Integer> response = new ArrayList<>();
        if (isCollapsed) {
            response.add(content);
            return response;
        }
        response = canIbe.keySet().stream().filter(key -> canIbe.get(key)).collect(Collectors.toList());
        return response;
    }

    public PropagationResponseEntity propagate(int whereIamRelativeToCaller, List<Integer> listOfPossibilitiesOfCaller) throws MapGenerationException {
        PropagationResponseEntity response = new PropagationResponseEntity(false, false, new ArrayList<>());
        if (isCollapsed) return response;
        List<Integer> listOfPossibilitiesOfSelf = getPossibleTileContentLeft();
        List<Integer> listOfPossibilitiesOfSelfAfterPropagation = dBinteractions.canThisBeHere(listOfPossibilitiesOfSelf,
                whereIamRelativeToCaller, listOfPossibilitiesOfCaller);
        listOfPossibilitiesOfSelf.removeAll(listOfPossibilitiesOfSelfAfterPropagation);
        if (!listOfPossibilitiesOfSelf.isEmpty()) {
            for (Integer discardedPossibility : listOfPossibilitiesOfSelf) {
                removePossibility(discardedPossibility);
            }
            if (listOfPossibilitiesOfSelfAfterPropagation.size() == 1) {
                return new PropagationResponseEntity(true, false, null);
            }
            response.setHasChangedPossibility(true);
            response.setNewPossibilities((ArrayList<Integer>) listOfPossibilitiesOfSelfAfterPropagation);
        }
        return response;
//        for (Integer possibleTile: getPossibleTileContentLeft()) {
//                if (!dBinteractions.canThisBeHere(possibleTile, whereIamRelativeToCaller, listOfPossibilitiesOfCaller)) {
//                    response.setHasChangedPossibility(true);
//                    if (removePossibility(possibleTile)) {
//                        return new PropagationResponseEntity(true, false, null);
//                    }
//                } else {
//                    ArrayList<Integer> listOfPossibilities = response.getNewPossibilities();
//                    listOfPossibilities.add(possibleTile);
//                    response.setNewPossibilities(listOfPossibilities);
//                }
//        }
    }

    private boolean removePossibility(int toRemove) {
        canIbe.replace(toRemove, false);
        possibleTileStatesLeft--;
        return possibleTileStatesLeft == 1;
    }

    public void collapse(int i) {
        if (!isCollapsed) {
            isCollapsed = true;
            content = i;
        }
    }

    public void collapse() throws MapGenerationException {
        if (isCollapsed) throw new MapGenerationException();
        collapse(getRandomState(getPossibleTileContentLeft()));
    }

    private int getRandomState(List<Integer> possibleStates) throws MapGenerationException {
        if (possibleStates.isEmpty()) {
            throw new RuntimeException("No Possible State");
        }

        HashMap<Integer, Integer> probabilities = dBinteractions.getProbabilityMap(possibleStates);
        int totalProbability = 0;
        for (Integer probability : probabilities.values()) {
            totalProbability += probability;
        }

        Random random = new Random();
        if (totalProbability <= 0) {
            return possibleStates.get(random.nextInt(possibleStates.size()));
        }

        int randomInt = random.nextInt(totalProbability);
        for (Integer possibleState : possibleStates) {
            randomInt -= probabilities.get(possibleState);
            if (randomInt < 0) {
                return possibleState;
            }
        }
        throw new MapGenerationException();
    }
}
