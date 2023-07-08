package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.propagation.BoardPropagationService;

public class BoardDamageControlService {
    public static void controlDamage(int problemTileX, int problemTileY, Board board) throws MapGenerationException {
        //TODO: Dmg Controll system ramps up the damage size if it's called again within it's own propagation
        //Maybe completely split put the damaged area, make it it's own board and give it to the Filling service
        int damageSize = 5;
        int damageAreaBorderMinX = Math.max(problemTileX - damageSize, 0);
        int damageAreaBorderMinY = Math.max(problemTileY - damageSize, 0);
        int damageAreaBorderMaxX = Math.min(problemTileX + damageSize, board.getWidth());
        int damageAreaBorderMaxY = Math.min(problemTileY + damageSize, board.getHeight());

        for (int x = damageAreaBorderMinX + 1; x < damageAreaBorderMaxX - 1; x++) {
            for (int y = damageAreaBorderMinY + 1; y < damageAreaBorderMaxY - 1; y++) {
                board.newTileAt(x, y);
            }
        }
        for (int x = damageAreaBorderMinX; x < damageAreaBorderMaxX; x++) {
           BoardPropagationService.startPropagation(x, damageAreaBorderMinY, board);
            BoardPropagationService.startPropagation(x, damageAreaBorderMaxY - 1, board);
        }
        for (int y = damageAreaBorderMinY; y < damageAreaBorderMaxY; y++) {
            BoardPropagationService.startPropagation(damageAreaBorderMinX, y, board);
            BoardPropagationService.startPropagation(damageAreaBorderMaxX - 1, y, board);
        }
    }
}
