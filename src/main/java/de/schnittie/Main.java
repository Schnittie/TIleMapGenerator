package de.schnittie;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardImageFactory;
import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.businesscode.board.splitting.BoardFusionException;
import de.schnittie.model.businesscode.board.splitting.BoardFusionFactory;
import de.schnittie.model.businesscode.board.splitting.BoardSplittingService;
import de.schnittie.model.businesscode.board.splitting.InvalidDimensionException;
import de.schnittie.model.database.InstallationHandler;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws MapGenerationException, InvalidDimensionException, BoardFusionException {
        InstallationHandler.generateTilesForDefaultMapIfNotPresent();
        Configuration.reloadConfiguration();

        Board board = new Board(160,160);
        HashMap<PairOfCoordinates, Board> boardCoordianteMap = BoardSplittingService.splitBoardIntoSmallerShelledBoards(board);
        //BoardFillingService.fill(boardCoordianteMap.get(new PairOfCoordinates(0,0)));
        Board fusedSubboard = BoardFusionFactory.fuseBoardsAlongXAxis(boardCoordianteMap.get(new PairOfCoordinates(0,0)),boardCoordianteMap.get(new PairOfCoordinates(0,1)));
        BoardImageFactory.generateBoardImage(fusedSubboard.getBoardTO());
        //Controller controller = new Controller();
    }
}
