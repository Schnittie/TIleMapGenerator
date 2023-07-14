package de.schnittie.model.businesscode.tile.tileObjects;

import de.schnittie.model.businesscode.MapGenerationException;

import java.util.List;

public interface Tile {
    public boolean propagate(int whereIamRelativeToCaller, List<Integer> listOfPossibilitiesOfCaller) throws MapGenerationException;
    public List<Integer> getPossibleTileContentLeft();
    public void removePossibility(int toRemove);
    public TileCollapsed collapse(int i);
    public Tile collapse() ;
    public boolean isCollapsed();
    public int getPossibleTileStatesLeft();
    public int getContent();
}
