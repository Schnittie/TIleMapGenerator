package de.schnittie.model.logic.damageControll;

import de.schnittie.model.logic.Configuration;
import de.schnittie.model.logic.MapGenerationException;
import de.schnittie.model.logic.board.Board;
import de.schnittie.model.logic.board.BoardCollapsingTileUtility;
import de.schnittie.model.logic.board.PairOfCoordinates;
import de.schnittie.model.logic.tile.TilePropagationUtility;
import de.schnittie.model.logic.tile.tileObjects.Tile;
import de.schnittie.model.logic.tile.tileObjects.TileInProgress;
import de.schnittie.model.database.DBinteractions;

import java.util.ArrayList;
import java.util.HashMap;


public class PatchApplierUtility {
    private static final HashMap<Integer, PairOfCoordinates> directionChangeMap = Configuration.getInstance().getDirectionChanges();
    private static final HashMap<Integer,Integer> reverseDirection = DBinteractions.getInstance().getReverseDirection();
    public static void applyPatch(PatchInstruction patchInstruction, Board board) {
        for (PairOfCoordinates coordinatesToReset : patchInstruction.resettingList()) {
            board.setTile(coordinatesToReset, new TileInProgress());
        }
        ArrayList<PairOfCoordinates> listOfTilesResetButNotInArea = new ArrayList<>(patchInstruction.resettingList());
        listOfTilesResetButNotInArea.removeAll(patchInstruction.damageArea());
        for (PairOfCoordinates coordinatesOfResetTile : listOfTilesResetButNotInArea) {
            propagateIntoMe(coordinatesOfResetTile, board);
        }
        BoardCollapsingTileUtility.forceCollapse(patchInstruction.damageArea(), board, patchInstruction.easyTileId());
    }

    private static void propagateIntoMe(PairOfCoordinates coordinatesOfResetTile, Board board) {
        Tile tileInProgress = board.getTile(coordinatesOfResetTile);
        if (tileInProgress.isCollapsed())return;
        for (Integer direction : directionChangeMap.keySet()) {
            try {
                TilePropagationUtility.propagate(reverseDirection.get(direction),board.getTile(
                        coordinatesOfResetTile.x()+directionChangeMap.get(direction).x(),
                        coordinatesOfResetTile.y() + directionChangeMap.get(direction).y()).
                        getPossibleTileContentLeft(),(TileInProgress) tileInProgress);
            } catch (MapGenerationException | IndexOutOfBoundsException e) {
                return;
            }
        }
    }
}
