package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardImageFactory;
import de.schnittie.model.businesscode.board.PairOfCoordinates;

import java.util.ArrayList;

public class BoardDamageControlService {
    private static ArrayList<ArrayList<Integer>> easyTilesSets = Configuration.getInstance().getEasyTiles();

    public static void controlDamage(int problemTileX, int problemTileY, Board board) throws MapGenerationException {
        BoardImageFactory.renderDamageImage(board.getBoardTO(), new PairOfCoordinates(problemTileX, problemTileY));
        System.out.println("Trying to applyPatch to "+ problemTileX + " "+ problemTileY );
        PatchInstruction patchInstruction = EasyTilePatchAreaFinderService.
                getPatchInstruction(new PairOfCoordinates(problemTileX, problemTileY), easyTilesSets, board);
        System.out.println("Applying Patch...");
        PatchApplierService.applyPatch(patchInstruction, board);
        System.out.println("Damage Fixed");
    }
}
