package de.schnittie.model.businesscode.board.propagation;

import de.schnittie.model.businesscode.board.PairOfCoordinates;

public record BoardPropagationToDo(PairOfCoordinates coordinates, BoardPropagationInstructionForOneTile instruction) {
}
