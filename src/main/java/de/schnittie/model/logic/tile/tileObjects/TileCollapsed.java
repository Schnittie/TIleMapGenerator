package de.schnittie.model.logic.tile.tileObjects;

import de.schnittie.model.logic.MapGenerationException;

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
        //nothing to do here
    }

    @Override
    public TileCollapsed collapse(int i) {
        //nothing to do here
         return  new TileCollapsed(i);
    }

    @Override
    public Tile collapse() {
        //nothing to do here
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
