import java.util.ArrayList;
import java.util.HashMap;

public record PropagationResultLists(ArrayList<Board.Pair> toCollapse, HashMap<Board.Pair, ArrayList<ETileContent>> toPropagate) {
}
