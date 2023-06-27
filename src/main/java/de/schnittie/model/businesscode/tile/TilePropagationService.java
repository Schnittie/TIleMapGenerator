package de.schnittie.model.businesscode.tile;

import de.schnittie.model.businesscode.MapGenerationException;

import java.util.ArrayList;
import java.util.List;

public class TilePropagationService {
    private static final TileDataProvider tileDataProvider = TileDataProvider.getInstance();
    public static boolean propagate(int whereIamRelativeToCaller, List<Integer> listOfPossibilitiesOfCaller, Tile tile)
            throws MapGenerationException {

        ArrayList<Integer> responseList = new ArrayList<>();
        if (tile.isCollapsed()) {
            return false;
        }

        List<Integer> listOfPossibilitiesOfSelf = tile.getPossibleTileContentLeft();
        List<Integer> listOfPossibilitiesOfSelfAfterPropagation = tileDataProvider.getPossibleAdjacencyProvider().canThisBeHere(listOfPossibilitiesOfSelf,
                whereIamRelativeToCaller, listOfPossibilitiesOfCaller);
        listOfPossibilitiesOfSelf.removeAll(listOfPossibilitiesOfSelfAfterPropagation);
        //all the possibilities that I can now be are removed, leaving listOfPossibilitiesOfSelf a
        // list of Possibilities that the Tile can no longer be

        if (listOfPossibilitiesOfSelf.isEmpty()) {
            //the possibilities didn't change
            return false;
        }
        for (Integer discardedPossibility : listOfPossibilitiesOfSelf) {
            tile.removePossibility(discardedPossibility);
        }
        if (listOfPossibilitiesOfSelfAfterPropagation.size() == 1) {
            //A tile with only one Possibility left has to collapse
            tile.collapse(listOfPossibilitiesOfSelfAfterPropagation.get(0));
            responseList.add(tile.getContent());
            return true;
        }
        return true;

    }
}
