import java.io.IOException;

public class Main {

    //TODO: working Directions when Creating rules
    //TODO: Bigger Images when Creating rules
    //TODO: Database Rules
    //TODO: Renderer Drawn from Tilemap

    //TODO: Frontend
    public static void main(String[] args) {
        int width = 200;
        int height = 200;
        try {
            Board b = new Board(width, height);
            b.fill();
            b.print();
            BoardImageGenerator.generateBoardImage(b.getBoard(), "generatedMapRender.png", height, width);
        } catch (MapGenerationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
