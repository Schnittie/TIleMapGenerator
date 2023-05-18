package de.schnittie.businesscode;

public class Main {

    //TODO: pull Square Positions in GUI from DB
    //TODO: Nuke extra Directions
    //TODO: Actually make Lowest Entropy Tiles return lowest Entropy Tiles (I'm not sure that works or should)

    //TODO: Frontend

    //TODO: new TIlemap (Lego Battles)

    //TODO: do something about the absolute file paths
    //TODO: proper DB architecture
    //TODO: Architecture in general
    //TODO: Model View Controller
    //TODO: Thread stuff for progressbar in Frontend und das die Buttons nicht blockieren
    //TODO: Ultra super fancy progress

    //TODO: I'm not sure but instead of always asking the database, how about we pull all the rules in at the start?

    //TODO: maybe rethink your whole rule structure ...

    public static void main(String[] args) {
        int width = 250;
        int height = 250;
        try {
            Board b = new Board(width, height);
            b.fill();
            BoardImageGenerator.generateBoardImage(b.getBoard(), "generatedMapRender.png", height, width);
        } catch (MapGenerationException e) {
            throw new RuntimeException(e);
        }
    }
}
