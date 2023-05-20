package de.schnittie.model.businesscode;

public class Main {
    //TODO: maybe rethink your whole rule structure ...
        //TODO: MVC architecture
        //TODO: Implement the code on paper
        //TODO: should only be rotated partway? (all get rotated then check if there are duplicates?)
        //TODO: Tiles start out with empty superposition, get possible tiles on first contact
        //TODO: make Random a singelton
        //TODO: rethink propagation and collapse
        //TODO: rethink the progressbar

    //TODO: Actually make Lowest Entropy Tiles return lowest Entropy Tiles (I'm not sure that works or should)

    //TODO: Frontend
        //TODO: Thread stuff for progressbar in Frontend und das die Buttons nicht blockieren
        //TODO: Ultra super fancy progress

    //TODO: new TIlemap (ask Lorenz)


    public static void main(String[] args) {
//        DirectionCreation.putDirectionsIntoDB();
//        TileCreation.putTilesInDB(new File("C:\\Users\\laure\\Desktop\\TilesTestingSockets\\Tilemap.png"), true);
//        TileCreation.putTilesInDB(new File("C:\\Users\\laure\\Desktop\\TilesTestingSockets\\TilemapNonRotate.png"), false);
//        RuleCreation.generateRules();
//        System.out.println("wow I made it this far");

        int width = 100;
        int height = 100;
        try {
            Board b = new Board(width, height);
            b.fill();
            BoardImageFactory.generateBoardImage(b.getBoard(), "generatedMapRender.png", height, width);
        } catch (MapGenerationException e) {
            throw new RuntimeException(e);
        }
    }
}
