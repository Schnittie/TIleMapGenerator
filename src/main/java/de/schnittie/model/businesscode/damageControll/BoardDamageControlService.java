package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardImageFactory;
import de.schnittie.model.businesscode.board.PairOfCoordinates;

import java.util.ArrayList;

// util?
public class BoardDamageControlService {

    // Having a package called "businesscode" in the "model" package is very strange.
    // the whole point behind having an "controller" package is to put business logic there.
    // right now you have like nothing in there, instead everything seems to live in "model"
    // Strong hint: if a class is called "[...]Service", you'd need a strong excuse to put it in "model" instead of
    // "controller". "model" is only for defining the data-model, not for processing them.
    // this applies to many classes here, not only this one, but I won't mark them all, since i think you got my point.
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
