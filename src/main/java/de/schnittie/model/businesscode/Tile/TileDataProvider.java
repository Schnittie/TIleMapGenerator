package de.schnittie.model.businesscode.Tile;

import de.schnittie.model.database.DBinteractions;

import java.util.ArrayList;

public class TileDataProvider {
    public static final TileDataProvider TILE_DATA_PROVIDER = new TileDataProvider();

    public static TileDataProvider getInstance() {
        return TILE_DATA_PROVIDER;
    }

    private final DBinteractions dBinteractions = DBinteractions.getInstance();
    private final ArrayList<Integer> possibleTileIDs = dBinteractions.getPossibleTileIDs();
    private final TileRandomCollapsingService tileRandomCollapsingService =
            new TileRandomCollapsingService(dBinteractions.getProbabilityMap(possibleTileIDs));

    private final PossibleAdjacencyProvider possibleAdjacencyProvider =
            new PossibleAdjacencyProvider(dBinteractions);

    public TileDataProvider() {
    }

    public DBinteractions getdBinteractions() {
        return dBinteractions;
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
