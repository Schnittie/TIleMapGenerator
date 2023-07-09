package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.board.PairOfCoordinates;

import java.util.ArrayList;

public record PatchInstruction(ArrayList<PairOfCoordinates> damageArea, int easyTileId) {
}
