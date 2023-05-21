package de.schnittie.model.businesscode;

import de.schnittie.model.database.DBinteractions;
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
    private final Random random;

    public Tile(int possibleTileStatesLeft, int[] possibleTileIDs, Random random) {
        this.possibleTileStatesLeft = possibleTileStatesLeft;
        canIbe = new HashMap<>(possibleTileStatesLeft);
        this.random = random;
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
        ArrayList<Integer> responseList = new ArrayList<>();
        PropagationResponseEntity response = new PropagationResponseEntity(false, false, responseList);
        if (isCollapsed) return response;
        if (possibleTileStatesLeft == 1) {
            collapse(getPossibleTileContentLeft().get(0));
            responseList.add(getContent());
            return new PropagationResponseEntity(false, true, responseList);
        }
        List<Integer> listOfPossibilitiesOfSelf = getPossibleTileContentLeft();
        List<Integer> listOfPossibilitiesOfSelfAfterPropagation = dBinteractions.canThisBeHere(listOfPossibilitiesOfSelf,
                whereIamRelativeToCaller, listOfPossibilitiesOfCaller);
        listOfPossibilitiesOfSelf.removeAll(listOfPossibilitiesOfSelfAfterPropagation);
        if (!listOfPossibilitiesOfSelf.isEmpty()) {
            for (Integer discardedPossibility : listOfPossibilitiesOfSelf) {
                removePossibility(discardedPossibility);
            }
            if (listOfPossibilitiesOfSelfAfterPropagation.size() == 1) {
                collapse(getPossibleTileContentLeft().get(0));
                responseList.add(getContent());
                return new PropagationResponseEntity(false, true, responseList);
            }
            response.setHasChangedPossibility(true);
            response.setNewPossibilities((ArrayList<Integer>) listOfPossibilitiesOfSelfAfterPropagation);
        }
        return response;
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
        if (!isCollapsed) {
            collapse(getRandomState(getPossibleTileContentLeft()));
        }
    }

    private int getRandomState(List<Integer> possibleStates) throws MapGenerationException {
        if (possibleStates.isEmpty()) {
            throw new RuntimeException("No Possible State");
        }
        if (possibleStates.size() == 1) {
            return possibleStates.get(0);
        }

        HashMap<Integer, Integer> probabilities = dBinteractions.getProbabilityMap(possibleStates);
        int totalProbability = 0;
        for (Integer probability : probabilities.values()) {
            totalProbability += probability;
        }

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
