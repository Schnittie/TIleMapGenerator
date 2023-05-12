package BusinessCode;

public class Main {

    //TODO: Database Rules
        //TODO: Nr. Of possible Tiles
        //TODO: Can this be here
        //TODO: getProbabilityMap
        //TODO: getDirectionChanges
        //TODO:
    //TODO: Renderer from DB
    //here it should work again
    //TODO: LightSQL or smt
    //TODO: pull Square Positions in GUI from DB
    //TODO: Actually make Lowest Entropy Tiles return lowest Entropy Tiles
    //TODO: Nuke extra Directions

    //TODO: Frontend
    //TODO: improve propagation list-list

    public static void main(String[] args) {
        int width = 100;
        int height = 100;
        try {
            Board b = new Board(width, height);
            b.fill();
            BoardImageGenerator.generateBoardImage(b.getBoard(), "generatedMapRender.png", height, width);
        } catch (MapGenerationException e) {
            throw new RuntimeException(e);
        }
    }
}
