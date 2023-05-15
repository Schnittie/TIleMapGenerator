package BusinessCode;

public class Main {

    //TODO: LightSQL or smt (WIP)
    //TODO: pull Square Positions in GUI from DB
    //TODO: Nuke extra Directions
    //TODO: Actually make Lowest Entropy Tiles return lowest Entropy Tiles

    //TODO: Frontend
    //TODO: improve propagation list-list

    //TODO: new TIlemap (Lego Battles)

    //TODO: do something about the absolute file paths

    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        try {
            Board b = new Board(width, height);
            b.fill();
            BoardImageGenerator.generateBoardImage(b.getBoard(), "generatedMapRender.png", height, width);
        } catch (MapGenerationException e) {
            throw new RuntimeException(e);
        }
    }
}
