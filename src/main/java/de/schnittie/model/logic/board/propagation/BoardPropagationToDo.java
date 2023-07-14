package de.schnittie.model.logic.board.propagation;

import de.schnittie.model.logic.board.PairOfCoordinates;

public record BoardPropagationToDo(PairOfCoordinates coordinates, BoardPropagationInstructionForOneTile instruction) {
}
