package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardCollapsingTileService;

public class PatchApplierService {
    public static void applyPatch(PatchInstruction patchInstruction, Board board) {
        BoardCollapsingTileService.forceCollapse(patchInstruction.damageBorder(), board, patchInstruction.easyTileId());
        BoardCollapsingTileService.forceCollapseWithoutPropagation(patchInstruction.damageArea(),board, patchInstruction.easyTileId());
    }
}
