import java.io.IOException;

public class Main {

    //TODO: More Directions
    //TODO: Tile detail
    //TODO:Fluss
    public static void main(String[] args) {
        int width = 100;
        int height = 100;
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
