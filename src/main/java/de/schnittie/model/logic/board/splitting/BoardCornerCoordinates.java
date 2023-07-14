package de.schnittie.model.logic.board.splitting;

import de.schnittie.model.logic.board.PairOfCoordinates;

public record BoardCornerCoordinates(PairOfCoordinates MinXMinY, PairOfCoordinates MaxXMinY, PairOfCoordinates MinXMaxY, PairOfCoordinates MaxXMaxY) {
}
