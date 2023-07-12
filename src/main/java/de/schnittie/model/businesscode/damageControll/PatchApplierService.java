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

//TODO
// util :3
public class PatchApplierService {
    private static final HashMap<Integer, PairOfCoordinates> directionChangeMap = Configuration.getInstance().getDirectionChanges();
    private static final HashMap<Integer,Integer> reverseDirection = DBinteractions.getInstance().getReverseDirection();
    public static void applyPatch(PatchInstruction patchInstruction, Board board) {
        System.out.println("Applying innerPatch...");
        if (isAnythingOnBorder(patchInstruction, board)){
            throw new RuntimeException("If this is thrown then theirs something wrong in code. sowwy ");
            //TODO delete anything that has to do with this, this should not be in prod code
        }
        for (PairOfCoordinates coordinatesToReset : patchInstruction.resettingList()) {
            board.setTile(coordinatesToReset, new TileInProgress());
        }
        ArrayList<PairOfCoordinates> listOfTilesResetButNotInArea = new ArrayList<>(patchInstruction.resettingList());
        listOfTilesResetButNotInArea.removeAll(patchInstruction.damageArea());
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
    private static boolean isAnythingOnBorder(PatchInstruction patchInstruction, Board board){
        for (PairOfCoordinates coords : patchInstruction.damageArea()) {
            if (!isInBoard(coords,board)||isOnBorder(coords,board))
                return true;
        }
        for (PairOfCoordinates coords : patchInstruction.resettingList()) {
            if (!isInBoard(coords,board)||isOnBorder(coords,board))
                return true;
        }
        return false;
    }
    private static boolean isOnBorder(PairOfCoordinates possibleBorderMember, Board board) {
        return possibleBorderMember.x() == 0 || possibleBorderMember.y() == 0 ||
                possibleBorderMember.x() == board.getWidth() - 1 || possibleBorderMember.y() == board.getHeight() - 1;
    }

    private static boolean isInBoard(PairOfCoordinates neighbour, Board board) {
        if (neighbour.y() < 0 || neighbour.x() < 0) return false;
        return neighbour.y() < board.getHeight() && neighbour.x() < board.getWidth();
    }
}
