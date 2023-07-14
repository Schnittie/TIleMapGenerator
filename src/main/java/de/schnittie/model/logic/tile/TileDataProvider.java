package de.schnittie.model.logic.tile;

import de.schnittie.model.logic.Configuration;

import java.util.ArrayList;

public class TileDataProvider {
    private static ThreadLocal<TileDataProvider> _threadLocal = ThreadLocal.withInitial(TileDataProvider::new);

    public static TileDataProvider getInstance() {
        return _threadLocal.get();
    }

    public static void reloadTileDataProvider() {
        _threadLocal = ThreadLocal.withInitial(TileDataProvider::new);
    }

    private final PossibleAdjacencyProvider possibleAdjacencyProvider = new PossibleAdjacencyProvider();
    private final TileRandomCollapsingService tileRandomCollapsingService = new TileRandomCollapsingService();

    public ArrayList<Integer> getPossibleTileIDs() {
        return Configuration.getInstance().getPossibleTileIDs();
    }

    public PossibleAdjacencyProvider getPossibleAdjacencyProvider() {
        return possibleAdjacencyProvider;
    }

    public TileRandomCollapsingService getTileRandomCollapsingService() {
        return tileRandomCollapsingService;
    }
}
