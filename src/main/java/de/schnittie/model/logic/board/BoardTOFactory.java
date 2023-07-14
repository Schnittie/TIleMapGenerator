package de.schnittie.model.businesscode.board;

public class BoardTOFactory {
    public static BoardTO getBoardTO(Board board) {
        int[][] ids = new int[board.getWidth()][board.getHeight()];
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                ids[x][y] = board.getTile(x, y).getContent();
            }
        }
        return new BoardTO(ids, board.getWidth(), board.getHeight());
    }
}
