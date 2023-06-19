package de.schnittie.model.businesscode.board;

public class BoardTOFactory {
    public static BoardTO getBoardTO(Board board) {
        int[][] ids = new int[board.getWIDTH()][board.getHEIGHT()];
        for (int x = 0; x < board.getWIDTH(); x++) {
            for (int y = 0; y < board.getHEIGHT(); y++) {
                ids[x][y] = board.getTile(x, y).getContent();
            }
        }
        return new BoardTO(ids, board.getWIDTH(), board.getHEIGHT(), null);
    }
}
