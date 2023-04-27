import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    private Tile[][] board;
    private final int WIDTH = 20; //x
    private final int HEIGHT =50;//y
    private final int NUMBEROFTILES = WIDTH * HEIGHT;
    private int numberCollapsed = 0;

    public Board() {
        board = new Tile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = new Tile();
            }

        }
        for (int y = 0; y < HEIGHT; y++) {
            board[0][y].collapse(RuleList.MOUNTAINID);
            propagate(0,y);
        }
        for (int y = 0; y < HEIGHT; y++) {
            board[WIDTH-1][y].collapse(RuleList.OCEANID);
            propagate(WIDTH-1,y);
        }
    }

    public void fill() throws IllegalArgumentException{
            while (numberCollapsed < NUMBEROFTILES) {
                Pair randomTileWithLowEntropy = getRandomTileWithLowEntropy();

                if (randomTileWithLowEntropy.x == -10) {
                    return;
                }
                int x = randomTileWithLowEntropy.x;
                int y = randomTileWithLowEntropy.y;
                collapseATile(x, y);
            }

    }

    private void collapseATile(int x, int y) throws IllegalArgumentException {
        //returns the amount of collapsed tiles
        board[x][y].collapse();
        if (!checkIfPlacedTileIsRight(x, y)) {
            throw new IllegalArgumentException();
        }
        propagate(x, y);
    }

    private void propagate(int x, int y) throws IllegalArgumentException {
        TileContent newTileContent = board[x][y].getContent();
        ArrayList<Pair> toCollapse = new ArrayList<>(4);
        if (x > 0 && board[x - 1][y].propagate(Direction.TOLEFT, newTileContent)) {
            toCollapse.add(new Pair(x - 1, y));
        }
        if (x < WIDTH - 1 && board[x + 1][y].propagate(Direction.TORIGHT, newTileContent)) {
            toCollapse.add(new Pair(x + 1, y));
        }
        if (y > 0 && board[x][y - 1].propagate(Direction.ABOVE, newTileContent)) {
            toCollapse.add(new Pair(x, y - 1));
        }
        if (y < HEIGHT - 1 && board[x][y + 1].propagate(Direction.BELOW, newTileContent)) {
            toCollapse.add(new Pair(x, y + 1));
        }
        for (Pair pair : toCollapse) {
            collapseATile(pair.x, pair.y);
        }
    }

    private Pair getRandomTileWithLowEntropy() {
        int lowestEntropy = TileContent.values().length;
        ArrayList<Pair> lowestEntropyTiles = new ArrayList<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (!board[x][y].isCollapsed() && board[x][y].getPossibleTileStatesLeft() <= lowestEntropy) {
                    if (board[x][y].getPossibleTileStatesLeft() < lowestEntropy) {
                        lowestEntropyTiles = new ArrayList<>();
                        lowestEntropy = board[x][y].getPossibleTileStatesLeft();
                    }
                    lowestEntropyTiles.add(new Pair(x, y));
                }
            }
        }
        if (lowestEntropyTiles.isEmpty()) {
            return new Pair(-10, -10);
        }
        Random random = new Random();
        return lowestEntropyTiles.get(random.nextInt(lowestEntropyTiles.size()));
    }

    private record Pair(int x, int y) {
        @Override
        public String toString() {
            return (x + " " + y);
        }
    }

    private boolean checkIfPlacedTileIsRight(int x, int y) {
        if (!board[x][y].isCollapsed()) {
            return true;
        }
        if (((x > 0 && board[x - 1][y].isCollapsed()) && !board[x - 1][y].getContent().getRuleList().canThisBeHere(Direction.TOLEFT, board[x][y].getContent()))) {
            return false;
        }
        if (((x < WIDTH-1 && board[x + 1][y].isCollapsed()) && !board[x + 1][y].getContent().getRuleList().canThisBeHere(Direction.TORIGHT, board[x][y].getContent()))) {
            return false;
        }
        if (((y > 0 && board[x][y-1].isCollapsed()) && !board[x][y-1].getContent().getRuleList().canThisBeHere(Direction.ABOVE, board[x][y].getContent()))) {
            return false;
        }
        if (((y < HEIGHT-1 && board[x][y+1].isCollapsed()) && !board[x][y+1].getContent().getRuleList().canThisBeHere(Direction.BELOW, board[x][y].getContent()))) {
            return false;
        }
        return true;
    }

    public String print() throws IOException {
        System.out.println("-----------------");
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int y = 0; y < HEIGHT; y++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < WIDTH; x++) {
                stringBuilder.append(" ");
                stringBuilder.append(board[x][y].isCollapsed() ? board[x][y].getContent().getCharacter() : "U");
                stringBuilder.append(" ");
            }
            System.out.println(stringBuilder);
            stringBuilder2.append(stringBuilder);
            stringBuilder2.append("\n");
        }
        File file = new File("C:\\Users\\laure\\IdeaProjects\\Lego Battles Map Generator\\output\\map.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(stringBuilder2.toString());
        writer.close();
        return stringBuilder2.toString();
    }
}
