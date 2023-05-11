package BusinessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
public class PropagationResponseEntity {
    private boolean hasCollapsed;
    private boolean hasChangedPossibility;
    private ArrayList<Integer> newPossibilities;
}
