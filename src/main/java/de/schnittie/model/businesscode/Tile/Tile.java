package de.schnittie.model.businesscode.Tile;

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
    private final TileDataProvider tileDataProvider;

    public Tile(int possibleTileStatesLeft, TileDataProvider tileDataProvider) {
        this.possibleTileStatesLeft = possibleTileStatesLeft;
        canIbe = new HashMap<>(possibleTileStatesLeft);
        this.tileDataProvider = tileDataProvider;
        for (Integer possibleTileID : tileDataProvider.getPossibleTileIDs()) {
            canIbe.put(possibleTileID, true);
        }
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

    public ArrayList<Integer> propagate(int whereIamRelativeToCaller, List<Integer> listOfPossibilitiesOfCaller)
            throws MapGenerationException {

        ArrayList<Integer> responseList = new ArrayList<>();
        if (isCollapsed) {
            return null;
        }

        List<Integer> listOfPossibilitiesOfSelf = getPossibleTileContentLeft();
        List<Integer> listOfPossibilitiesOfSelfAfterPropagation = tileDataProvider.getPossibleAdjacencyProvider().canThisBeHere(listOfPossibilitiesOfSelf,
                whereIamRelativeToCaller, listOfPossibilitiesOfCaller);
        listOfPossibilitiesOfSelf.removeAll(listOfPossibilitiesOfSelfAfterPropagation);
        //all the possibilities that I can now be are removed, leaving listOfPossibilitiesOfSelf a
        // list of Possibilities that the Tile can no longer be

        if (listOfPossibilitiesOfSelf.isEmpty()) {
            //the possibilities didn't change
            return null;
        }
        for (Integer discardedPossibility : listOfPossibilitiesOfSelf) {
            removePossibility(discardedPossibility);
        }
        if (listOfPossibilitiesOfSelfAfterPropagation.size() == 1) {
            //A tile with only one Possibility left has to collapse
            collapse(getPossibleTileContentLeft().get(0));
            responseList.add(getContent());
            return responseList;
        }
        return (ArrayList<Integer>) listOfPossibilitiesOfSelfAfterPropagation;

    }

    private void removePossibility(int toRemove) {
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
