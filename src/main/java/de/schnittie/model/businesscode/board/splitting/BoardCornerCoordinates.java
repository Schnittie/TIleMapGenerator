package de.schnittie.model.businesscode.board.splitting;

import de.schnittie.model.businesscode.board.PairOfCoordinates;

public record BoardCornerCoordinates(PairOfCoordinates MinXMinY, PairOfCoordinates MaxXMinY, PairOfCoordinates MinXMaxY, PairOfCoordinates MaxXMaxY) {
}
