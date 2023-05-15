package BusinessCode;

public class Main {

    //TODO: LightSQL or smt
    //TODO: pull Square Positions in GUI from DB
    //TODO: Nuke extra Directions
    //TODO: Actually make Lowest Entropy Tiles return lowest Entropy Tiles

    //TODO: Frontend
    //TODO: improve propagation list-list

    //TODO: new TIlemap (Lego Battles?)

    public static void main(String[] args) {
        int width = 50;
        int height = 50;
        try {
            Board b = new Board(width, height);
            b.fill();
            BoardImageGenerator.generateBoardImage(b.getBoard(), "generatedMapRender.png", height, width);
        } catch (MapGenerationException e) {
            throw new RuntimeException(e);
        }
    }
}
