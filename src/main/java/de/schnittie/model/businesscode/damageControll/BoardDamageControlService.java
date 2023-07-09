package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardImageFactory;
import de.schnittie.model.businesscode.board.PairOfCoordinates;

public class BoardDamageControlService {
    public static void controlDamage(int problemTileX, int problemTileY, Board board) throws MapGenerationException {
        //TODO: Dmg Controll system ramps up the damage size if it's called again within it's own propagation
        //Maybe completely split put the damaged area, make it it's own board and give it to the Filling service

        BoardImageFactory.renderDamageImage(board.getBoardTO(), new PairOfCoordinates(problemTileX, problemTileY));
        PatchApplierService.applyPatch(EasyTilePatchAreaFinderService.getPatchInstruction(
                new PairOfCoordinates(problemTileX, problemTileY),
                EasyTileFinderService.findEasyTileSets(), board), board);
    }
}
