import java.io.IOException;

public class Main {

    //TODO: Free generation
    //TODO: More Directions

    //TODO:Fluss
    public static void main(String[] args) throws IOException, MapGenerationException {
        int trialcound = 0;
        boolean t = true;
        Board b = new Board();
        while (t) {
            t = false;
            try {
                b = new Board();
                b.fill();
            } catch (MapGenerationException e) {
                throw new RuntimeException(e);
            }
        }
        b.print();
        System.out.println(trialcound);
    }
}
