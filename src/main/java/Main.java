public class Main {

    //TODO: Database Rules
        //TODO: Nr. Of possible Tiles
        //TODO: Can this be here
        //TODO: getProbabilityMap
        //TODO: getDirectionChanges
        //TODO: getPossibleTileIDs
    //TODO: Nuke extra Directions
    //TODO: Actually make Lowest Entropy Tiles return lowest Entropy Tiles

    //TODO: Frontend
    //TODO: improve propagation list-list

    public static void main(String[] args) {
        int width = 200;
        int height = 200;
        try {
            Board b = new Board(width, height);
            b.fill();
            BoardImageGenerator.generateBoardImage(b.getBoard(), "generatedMapRender.png", height, width);
        } catch (MapGenerationException e) {
            throw new RuntimeException(e);
        }
    }
}
