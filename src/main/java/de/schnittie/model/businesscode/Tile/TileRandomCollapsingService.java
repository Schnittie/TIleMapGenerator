package de.schnittie.model.businesscode.Tile;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.database.DBinteractions;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TileRandomCollapsingService {
    private Random random = new Random();
    private final DBinteractions dBinteractions = DBinteractions.getInstance();
    public int getRandomState(List<Integer> possibleStates)
            throws MapGenerationException {
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
