package de.schnittie.model.businesscode.Tile;

import de.schnittie.model.database.DBinteractions;

public class TileSingeltonService {
    public static final TileSingeltonService tileSingeltonService = new TileSingeltonService();
    public static TileSingeltonService getInstance(){
        return tileSingeltonService;
    }
    public final TileRandomCollapsingService tileRandomCollapsingService = new TileRandomCollapsingService();
    private final DBinteractions dBinteractions = DBinteractions.getInstance();
    public TileSingeltonService() {
    }
    public DBinteractions getdBinteractions(){
        return dBinteractions;
    }
    public TileRandomCollapsingService getTileRandomCollapsingService(){
        return tileRandomCollapsingService;
    }
}
