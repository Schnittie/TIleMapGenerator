package de.schnittie.model.businesscode.Tile;

import de.schnittie.model.database.DBinteractions;

public class TileSingletonService {
    public static final TileSingletonService TILE_SINGLETON_SERVICE = new TileSingletonService();
    public static TileSingletonService getInstance(){
        return TILE_SINGLETON_SERVICE;
    }
    public final TileRandomCollapsingService tileRandomCollapsingService = new TileRandomCollapsingService();
    private final DBinteractions dBinteractions = DBinteractions.getInstance();
    public TileSingletonService() {
    }
    public DBinteractions getdBinteractions(){
        return dBinteractions;
    }
    public TileRandomCollapsingService getTileRandomCollapsingService(){
        return tileRandomCollapsingService;
    }
}
