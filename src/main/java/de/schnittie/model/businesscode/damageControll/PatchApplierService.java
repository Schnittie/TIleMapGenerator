package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardCollapsingTileService;

public class PatchApplierService {
    public static void applyPatch(PatchInstruction patchInstruction, Board board) {
        System.out.println("Applying innerPatch...");
        BoardCollapsingTileService.forceCollapse(patchInstruction.damageArea(),board, patchInstruction.easyTileId());
        System.out.println("Patch applied");
    }
}
