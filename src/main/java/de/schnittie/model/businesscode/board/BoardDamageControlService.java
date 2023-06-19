package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.MapGenerationException;

public class BoardDamageControlService {
    public static void controlDamage(int problemTileX, int problemTileY, Board board) throws MapGenerationException {
        int damageSize = (board.getWIDTH() * board.getHEIGHT() >= 1000 ? 10 : 5);
        int damageAreaBorderMinX = Math.max(problemTileX - damageSize, 0);
        int damageAreaBorderMinY = Math.max(problemTileY - damageSize, 0);
        int damageAreaBorderMaxX = Math.min(problemTileX + damageSize, board.getWIDTH());
        int damageAreaBorderMaxY = Math.min(problemTileY + damageSize, board.getHEIGHT());

        for (int x = damageAreaBorderMinX + 1; x < damageAreaBorderMaxX - 1; x++) {
            for (int y = damageAreaBorderMinY + 1; y < damageAreaBorderMaxY - 1; y++) {
                board.newTileAt(x, y);
            }
        }
        for (int x = damageAreaBorderMinX; x < damageAreaBorderMaxX; x++) {
           BoardPropagationService.propagate(x, damageAreaBorderMinY, board);
            BoardPropagationService.propagate(x, damageAreaBorderMaxY - 1, board);
        }
        for (int y = damageAreaBorderMinY; y < damageAreaBorderMaxY; y++) {
            BoardPropagationService.propagate(damageAreaBorderMinX, y, board);
            BoardPropagationService.propagate(damageAreaBorderMaxX - 1, y, board);
        }
    }
}
