package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.board.PairOfCoordinates;

import java.util.ArrayList;
import java.util.Queue;

public record PatchInstruction(ArrayList<PairOfCoordinates> damageArea, Queue<PairOfCoordinates> possibleBorderQueue,
                               ArrayList<PairOfCoordinates> resettingList, int easyTileId) {
}
