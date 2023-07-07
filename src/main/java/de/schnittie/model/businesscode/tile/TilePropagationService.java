package de.schnittie.model.businesscode.tile;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.tile.tileObjects.TileInProgress;

import java.util.ArrayList;
import java.util.List;

public class TilePropagationService {
    private static final TileDataProvider tileDataProvider = TileDataProvider.getInstance();
    public static boolean propagate(int whereIamRelativeToCaller, List<Integer> listOfPossibilitiesOfCaller, TileInProgress tileInProgress)
            throws MapGenerationException {

        ArrayList<Integer> responseList = new ArrayList<>();
        if (tileInProgress.isCollapsed()) {
            return false;
        }

        List<Integer> listOfPossibilitiesOfSelf = tileInProgress.getPossibleTileContentLeft();
        List<Integer> listOfPossibilitiesOfSelfAfterPropagation = tileDataProvider.getPossibleAdjacencyProvider().canThisBeHere(listOfPossibilitiesOfSelf,
                whereIamRelativeToCaller, listOfPossibilitiesOfCaller);
        listOfPossibilitiesOfSelf.removeAll(listOfPossibilitiesOfSelfAfterPropagation);
        //all the possibilities that I can now be are removed, leaving listOfPossibilitiesOfSelf a
        // list of Possibilities that the TileInProgress can no longer be

        if (listOfPossibilitiesOfSelf.isEmpty()) {
            //the possibilities didn't change
            return false;
        }
        for (Integer discardedPossibility : listOfPossibilitiesOfSelf) {
            tileInProgress.removePossibility(discardedPossibility);
        }
        if (listOfPossibilitiesOfSelfAfterPropagation.size() == 1) {
            //A tileInProgress with only one Possibility left has to collapse
            tileInProgress.collapse(listOfPossibilitiesOfSelfAfterPropagation.get(0));
            responseList.add(tileInProgress.getContent());
        }
        return true;

    }
}
