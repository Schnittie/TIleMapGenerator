package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardImageFactory;
import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.businesscode.tile.tileObjects.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardDamageControlService {
    private static ArrayList<ArrayList<Integer>> easyTilesSets = Configuration.getInstance().getEasyTiles();
    private static HashMap<Integer,PairOfCoordinates> directionChanges = Configuration.getInstance().getDirectionChanges();
    private static HashMap<Integer,String> filePathMap = Configuration.getInstance().getFilePathMap();

    public static void controlDamage(int problemTileX, int problemTileY, Board board) throws MapGenerationException {
        BoardImageFactory.renderDamageImage(board.getBoardTO(), new PairOfCoordinates(problemTileX, problemTileY));
        for (Integer direction : directionChanges.keySet()) {
            try {
                Tile neighbouringTile = board.getTile(problemTileX +
                        directionChanges.get(direction).x(),problemTileY + directionChanges.get(direction).y());
                System.out.println("Direction: " + direction + " is Collapsed: " + neighbouringTile.isCollapsed() +
                        " States Left: " + neighbouringTile.getPossibleTileStatesLeft() +
                        " Possibilities Left: " + neighbouringTile.getPossibleTileContentLeft() +
                        " Content: " + neighbouringTile.getContent() +
                        " Filepath: " + (filePathMap.containsKey(neighbouringTile.getContent())?
                        filePathMap.get(neighbouringTile.getContent()) : " undefined"));
            } catch (IndexOutOfBoundsException e){
                //do nothing
            }
        }
        System.out.println("Trying to applyPatch to "+ problemTileX + " "+ problemTileY );
        PatchInstruction patchInstruction = EasyTilePatchAreaFinderService.
                getPatchInstruction(new PairOfCoordinates(problemTileX, problemTileY), easyTilesSets, board);
        System.out.println("Applying Patch...");
        PatchApplierService.applyPatch(patchInstruction, board);
        System.out.println("Damage Fixed");
    }
}
