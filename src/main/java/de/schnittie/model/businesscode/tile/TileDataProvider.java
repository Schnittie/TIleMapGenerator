package de.schnittie.model.businesscode.tile;

import de.schnittie.model.businesscode.Configuration;

import java.util.ArrayList;

public class TileDataProvider {
    public static final TileDataProvider tileDataProvider = new TileDataProvider();

    public static TileDataProvider getInstance() {
        return tileDataProvider;
    }

    private final ArrayList<Integer> possibleTileIDs = Configuration.getPossibleTileIDs();
    private final TileRandomCollapsingService tileRandomCollapsingService = new TileRandomCollapsingService();

    private final PossibleAdjacencyProvider possibleAdjacencyProvider = new PossibleAdjacencyProvider();

    public TileDataProvider() {
    }

    public PossibleAdjacencyProvider getPossibleAdjacencyProvider() {
        return possibleAdjacencyProvider;
    }

    public TileRandomCollapsingService getTileRandomCollapsingService() {
        return tileRandomCollapsingService;
    }

    public ArrayList<Integer> getPossibleTileIDs() {
        return possibleTileIDs;
    }
}
