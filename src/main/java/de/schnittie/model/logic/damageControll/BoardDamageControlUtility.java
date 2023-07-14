package de.schnittie.model.logic.damageControll;

import de.schnittie.model.logic.Configuration;
import de.schnittie.model.logic.MapGenerationException;
import de.schnittie.model.logic.board.Board;
import de.schnittie.model.logic.board.PairOfCoordinates;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardDamageControlUtility {

    private static ArrayList<ArrayList<Integer>> easyTilesSets = Configuration.getInstance().getEasyTiles();
    private static HashMap<Integer,PairOfCoordinates> directionChanges = Configuration.getInstance().getDirectionChanges();
    private static HashMap<Integer,String> filePathMap = Configuration.getInstance().getFilePathMap();

    public static void controlDamage(int problemTileX, int problemTileY, Board board) throws MapGenerationException {
        PatchInstruction patchInstruction = EasyTilePatchAreaFinderUtility.
                getPatchInstruction(new PairOfCoordinates(problemTileX, problemTileY), easyTilesSets, board);
        PatchApplierUtility.applyPatch(patchInstruction, board);
    }
}
