import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int trialcound = 0;
        boolean t = true;
        Board b = new Board();
        while (t) {
            t = false;
            try {
                b = new Board();
                b.fill();
            } catch (IllegalArgumentException e) {
                t = true;
                trialcound++;
            }
        }
        b.print();
        System.out.println(trialcound);
    }
}
