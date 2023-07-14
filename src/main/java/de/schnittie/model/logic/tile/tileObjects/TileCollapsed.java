package de.schnittie.model.businesscode.tile.tileObjects;

import de.schnittie.model.businesscode.MapGenerationException;

import java.util.ArrayList;
import java.util.List;

public class TileCollapsed implements Tile {
    private int tileContent;

    public TileCollapsed(int tileContent) {
        this.tileContent = tileContent;
    }

    @Override
    public boolean propagate(int whereIamRelativeToCaller, List<Integer> listOfPossibilitiesOfCaller) throws MapGenerationException {
        return false;
    }

    @Override
    public List<Integer> getPossibleTileContentLeft() {
        List<Integer> tileContentReturnList = new ArrayList<>(1);
        tileContentReturnList.add(tileContent);
        return tileContentReturnList;
    }

    @Override
    public void removePossibility(int toRemove) {
        //nothing to do here (shouldn't be called)
        throw new RuntimeException("This shouldn be called on a collapsed Tile");
        //TODO
        // maybe this shouldn't be part of the "Tile" interface then?
        // this exception implies, that a caller knows, that this shouldn't be called.
        // this again implies, that a TileInProgress is known to be one by a caller.
        // Just cast it to "TileInProgress" then and call this method, which is only implemented in "TileInProgress"
    }

    @Override
    public TileCollapsed collapse(int i) {
        //nothing to do here (shouldn't be called)
         return  new TileCollapsed(i);
    }

    @Override
    public Tile collapse() {
        //nothing to do here (shouldn't be called)
        return this;
    }

    @Override
    public boolean isCollapsed() {
        return true;
    }

    @Override
    public int getPossibleTileStatesLeft() {
        return 1;
    }

    @Override
    public int getContent() {
        return tileContent;
    }
}
