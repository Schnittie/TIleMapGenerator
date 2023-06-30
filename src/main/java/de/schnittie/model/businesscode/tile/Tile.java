package de.schnittie.model.businesscode.tile;

import de.schnittie.model.businesscode.MapGenerationException;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Tile {
    private int possibleTileStatesLeft;
    private final HashMap<Integer, Boolean> canIbe;
    private boolean isCollapsed = false;
    private int content = -1; //the default ID. to be overwritten when collapsed
    private final TileDataProvider tileDataProvider = TileDataProvider.getInstance();

    public Tile() {
        possibleTileStatesLeft = tileDataProvider.getPossibleTileIDs().size();
        canIbe = new HashMap<>(possibleTileStatesLeft);
        for (Integer possibleTileID : tileDataProvider.getPossibleTileIDs()) {
            canIbe.put(possibleTileID, true);
        }
    }
    public boolean propagate(int whereIamRelativeToCaller, List<Integer> listOfPossibilitiesOfCaller)
            throws MapGenerationException {
        return tileDataProvider.getTilePropagationProvider()
                .propagate(whereIamRelativeToCaller,listOfPossibilitiesOfCaller, this);
    }
    public List<Integer> getPossibleTileContentLeft() {
        //returns List of all the possibilities that this Tile can still be
        List<Integer> response = new ArrayList<>();
        if (isCollapsed) {
            response.add(content);
            return response;
        }
        response = canIbe.keySet().stream().filter(canIbe::get).collect(Collectors.toList());
        return response;
    }

    public void removePossibility(int toRemove) {
        canIbe.replace(toRemove, false);
        possibleTileStatesLeft--;
    }

    public void collapse(int i) {
        if (!isCollapsed) {
            isCollapsed = true;
            content = i;
        }
    }

    public void collapse() throws MapGenerationException {
        //collapsing, but the caller doesn't care which possible state the Tile ends up in
        if (!isCollapsed) {
            collapse(tileDataProvider.getTileRandomCollapsingService().getRandomState(getPossibleTileContentLeft()));
        }
    }

}
