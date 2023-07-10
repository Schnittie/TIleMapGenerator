package de.schnittie.model.businesscode.tile;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TileRandomCollapsingService {
    private final Random random = new Random();

    public TileRandomCollapsingService() {
    }

    public int getRandomState(List<Integer> possibleStates)
            throws MapGenerationException {
        if (possibleStates.isEmpty()) {
            // sounds more like an IllegalArgumentException here...
            throw new RuntimeException("No Possible State");
        }
        if (possibleStates.size() == 1) {
            return possibleStates.get(0);
        }

        HashMap<Integer, Integer> probabilities = getProbabilityMap(possibleStates);
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
        System.out.println("Error1");
        throw new MapGenerationException();
    }

    private HashMap<Integer, Integer> getProbabilityMap(List<Integer> possibleStates) {
        HashMap<Integer, Integer> returnMap = new HashMap<>(possibleStates.size());
        for (Integer tileId : possibleStates) {
            returnMap.put(tileId, Configuration.getInstance().getProbabilityMap().get(tileId));
        }
        return returnMap;
    }
}
