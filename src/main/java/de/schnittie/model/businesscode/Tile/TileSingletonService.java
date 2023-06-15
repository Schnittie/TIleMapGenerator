package de.schnittie.model.businesscode.Tile;

import de.schnittie.model.database.DBinteractions;

public class TileSingletonService {
    public static final TileSingletonService tileSingletonService = new TileSingletonService();
    public static TileSingletonService getInstance(){
        return tileSingletonService;
    }
    private final DBinteractions dBinteractions = DBinteractions.getInstance();
    public final TileRandomCollapsingService tileRandomCollapsingService = new TileRandomCollapsingService(dBinteractions.getProbabilityMap(dBinteractions.getPossibleTileIDs()));
    public TileSingletonService() {
    }
    public DBinteractions getdBinteractions(){
        return dBinteractions;
    }
    public TileRandomCollapsingService getTileRandomCollapsingService(){
        return tileRandomCollapsingService;
    }
}
