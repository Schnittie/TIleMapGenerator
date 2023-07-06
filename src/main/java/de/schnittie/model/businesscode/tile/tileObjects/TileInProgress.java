package de.schnittie.model.businesscode.tile.tileObjects;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.tile.TileDataProvider;
import de.schnittie.model.businesscode.tile.TilePropagationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TileInProgress implements Tile {
    private int possibleTileStatesLeft;
    private final HashMap<Integer, Boolean> canIbe;
    private boolean isCollapsed = false;
    private int content = -1; //the default ID. to be overwritten when collapsed
    private final TileDataProvider tileDataProvider = TileDataProvider.getInstance();

    public TileInProgress() {
        possibleTileStatesLeft = tileDataProvider.getPossibleTileIDs().size();
        canIbe = new HashMap<>(possibleTileStatesLeft);
        for (Integer possibleTileID : tileDataProvider.getPossibleTileIDs()) {
            canIbe.put(possibleTileID, true);
        }
    }
    public boolean propagate(int whereIamRelativeToCaller, List<Integer> listOfPossibilitiesOfCaller)
            throws MapGenerationException {
        return TilePropagationService.propagate(whereIamRelativeToCaller,listOfPossibilitiesOfCaller, this);
    }
    public List<Integer> getPossibleTileContentLeft() {
        //returns List of all the possibilities that this TileInProgress can still be
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

    public TileCollapsed collapse(int i) {
        if (!isCollapsed) {
            isCollapsed = true;
            content = i;
        }
        return new TileCollapsed(content);
    }

    public TileCollapsed collapse() {
        //collapsing, but the caller doesn't care which possible state the TileInProgress ends up in
        if (!isCollapsed) {
            try {
                return collapse(tileDataProvider.getTileRandomCollapsingService().getRandomState(getPossibleTileContentLeft()));
            } catch (MapGenerationException e) {
                throw new RuntimeException("Could not come up with Random state ");
            }
        }
        return new TileCollapsed(content);
    }
    public boolean isCollapsed(){
        return isCollapsed;
    }

    public int getPossibleTileStatesLeft() {
        return possibleTileStatesLeft;
    }

    public int getContent() {
        return content;
    }
}
