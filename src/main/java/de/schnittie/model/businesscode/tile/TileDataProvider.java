package de.schnittie.model.businesscode.tile;

import de.schnittie.model.businesscode.Configuration;

import java.util.ArrayList;

public class TileDataProvider {
    private static TileDataProvider tileDataProvider = new TileDataProvider();
    public static TileDataProvider getInstance() {
        return tileDataProvider;
    }
    public static void reloadTileDataProvider(){
        tileDataProvider = new TileDataProvider();
    }

    private final PossibleAdjacencyProvider possibleAdjacencyProvider = new PossibleAdjacencyProvider();
    private final TilePropagationProvider tilePropagationProvider = new TilePropagationProvider();
    private final TileRandomCollapsingService tileRandomCollapsingService = new TileRandomCollapsingService();

    public TileDataProvider() {
    }

    public ArrayList<Integer> getPossibleTileIDs() {
        return Configuration.getInstance().getPossibleTileIDs();
    }

    public PossibleAdjacencyProvider getPossibleAdjacencyProvider() {
        return possibleAdjacencyProvider;
    }

    public TileRandomCollapsingService getTileRandomCollapsingService() {
        return tileRandomCollapsingService;
    }

    public TilePropagationProvider getTilePropagationProvider() {
        return tilePropagationProvider;
    }
}
