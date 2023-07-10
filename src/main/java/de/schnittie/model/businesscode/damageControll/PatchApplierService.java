package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardCollapsingTileService;
import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.businesscode.tile.TilePropagationService;
import de.schnittie.model.businesscode.tile.tileObjects.Tile;
import de.schnittie.model.businesscode.tile.tileObjects.TileInProgress;
import de.schnittie.model.database.DBinteractions;

import java.util.ArrayList;
import java.util.HashMap;

public class PatchApplierService {
    private static final HashMap<Integer,PairOfCoordinates> directionChangeMap = Configuration.getInstance().getDirectionChanges();
    private static final HashMap<Integer,Integer> reverseDirection = DBinteractions.getInstance().getReverseDirection();
    public static void applyPatch(PatchInstruction patchInstruction, Board board) {
        System.out.println("Applying innerPatch...");
        for (PairOfCoordinates coordinatesToReset : patchInstruction.resettingList()) {
            board.setTile(coordinatesToReset, new TileInProgress());
        }
        ArrayList<PairOfCoordinates> listOfTilesResetButNotInArea = new ArrayList<>(patchInstruction.resettingList());
        listOfTilesResetButNotInArea.retainAll(patchInstruction.damageArea());
        for (PairOfCoordinates coordinatesOfResetTile : listOfTilesResetButNotInArea) {
            propagateIntoMe(coordinatesOfResetTile, board);
        }
        BoardCollapsingTileService.forceCollapse(patchInstruction.damageArea(), board, patchInstruction.easyTileId());
        System.out.println("Patch applied");
    }

    private static void propagateIntoMe(PairOfCoordinates coordinatesOfResetTile, Board board) {
        Tile tileInProgress = board.getTile(coordinatesOfResetTile);
        if (tileInProgress.isCollapsed())return;
        for (Integer direction : directionChangeMap.keySet()) {
            try {
                TilePropagationService.propagate(reverseDirection.get(direction),board.getTile(
                        coordinatesOfResetTile.x()+directionChangeMap.get(direction).x(),
                        coordinatesOfResetTile.y() + directionChangeMap.get(direction).y()).
                        getPossibleTileContentLeft(),(TileInProgress) tileInProgress);
            } catch (MapGenerationException | IndexOutOfBoundsException e) {
                return;
            }
        }
    }
}
