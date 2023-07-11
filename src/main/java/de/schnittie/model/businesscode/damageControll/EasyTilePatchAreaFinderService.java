package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.businesscode.tile.TilePropagationService;
import de.schnittie.model.businesscode.tile.tileObjects.TileInProgress;
import de.schnittie.model.database.DBinteractions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

//TODO
// util?
public class EasyTilePatchAreaFinderService {
    private static final int DAMAGE_MIN_SIZE = 1;
    private static final HashMap<Integer, PairOfCoordinates> directionChanges = Configuration.getInstance().getDirectionChanges();
    private static final HashMap<Integer, Integer> reverseDirection = DBinteractions.getInstance().getReverseDirection();

    public static PatchInstruction getPatchInstruction(PairOfCoordinates damageOrigin, ArrayList<ArrayList<Integer>> easyTileLists, Board board) {
        ArrayList<Integer> easyTileSetExamples = new ArrayList<>(easyTileLists.size());
        for (ArrayList<Integer> easyTileList : easyTileLists) {
            easyTileSetExamples.add(easyTileList.get(0));
        }
        // all these printlns should probably be replaced by a logger... its fine for development, but you'll like
        // not to bother the users with pointless logging stuff they'll never see anyways while
        // eating CPU-Cycles like they were crisps. A logger can be configured to shut up for prod builds.
        // this applies to every single println in your code.
        System.out.println("Trying to make out a damage Area...");
        return tryToFindPatchArea(damageOrigin, easyTileSetExamples, board);
    }

    private static PatchInstruction tryToFindPatchArea(PairOfCoordinates damageOrigin, ArrayList<Integer> easyTileExampleIds, Board board) {
        board.setTile(damageOrigin, new TileInProgress());
        HashMap<Integer, PatchInstruction> easyTileToPatchMap = new HashMap<>();
        for (Integer easyTileExampleId : easyTileExampleIds) {
            easyTileToPatchMap.put(easyTileExampleId, (new PatchInstruction(new ArrayList<>(), new LinkedList<>(), new ArrayList<>(), easyTileExampleId)));
            prefillQueue(damageOrigin, board, easyTileToPatchMap.get(easyTileExampleId).damageArea(),
                    easyTileToPatchMap.get(easyTileExampleId).possibleBorderQueue());
            easyTileToPatchMap.get(easyTileExampleId).possibleBorderQueue().add(damageOrigin);
            easyTileToPatchMap.get(easyTileExampleId).resettingList().addAll(easyTileToPatchMap.get(easyTileExampleId).damageArea());
        }

        PatchInstruction foundInstruction;
        while (true) {
            for (Integer easyTileExampleId : easyTileToPatchMap.keySet()) {
                foundInstruction = workThroughOneQueueElement(easyTileToPatchMap.get(easyTileExampleId), board);
                if (foundInstruction != null) return foundInstruction;
            }
        }
    }

    private static PatchInstruction workThroughOneQueueElement(PatchInstruction patchInstruction, Board board) {
        if (patchInstruction.possibleBorderQueue().isEmpty()) return patchInstruction;
        PairOfCoordinates possibleBorderMember = patchInstruction.possibleBorderQueue().poll();
//            if (isOnBorder(possibleBorderMember, board)) {
//                System.out.println("No valid Area found for tile ");
//                return null;
//            }
        if (!patchInstruction.damageArea().contains(possibleBorderMember)) {
            patchInstruction.damageArea().add(possibleBorderMember);
            patchInstruction.resettingList().add(possibleBorderMember);
        }
        HashMap<Integer, PairOfCoordinates> allNeighboursNotInArea = getAllNeighboursNotInArea(possibleBorderMember, patchInstruction.damageArea(), board);
        if (board.getTile(possibleBorderMember).getPossibleTileContentLeft().contains(patchInstruction.easyTileId())) {
            return null;
        } else if (couldBeGreenIfItWereNew(board, allNeighboursNotInArea, patchInstruction.easyTileId())) {
            return null;
        } else if (couldBeGreenIfNeighboursWereDifferent(board, allNeighboursNotInArea, patchInstruction.easyTileId(), patchInstruction.damageArea())) {
            patchInstruction.resettingList().addAll(allNeighboursNotInArea.values());
        }
        putAllNeighboursInQueueIfNotPresent(possibleBorderMember, patchInstruction.possibleBorderQueue(), patchInstruction.damageArea(), board);
        return null;
    }

    private static boolean couldBeGreenIfNeighboursWereDifferent(Board board, HashMap<Integer, PairOfCoordinates> allNeighboursNotInArea, int easyTile, ArrayList<PairOfCoordinates> patchArea) {
        for (Integer direction : allNeighboursNotInArea.keySet()) {
            if (!couldBeAdjacentToGreenIfItWereNew(board, easyTile, direction, getAllNeighboursNotInArea(allNeighboursNotInArea.get(direction), patchArea, board))) {
                return false;
            }
        }
        return true;
    }

    private static boolean couldBeAdjacentToGreenIfItWereNew(Board board, int easyTile, Integer direction,
                                                             HashMap<Integer, PairOfCoordinates> allNeighboursNotInArea) {
        TileInProgress potentialTile = new TileInProgress();
        ArrayList<Integer> easyTilePossibilityAsList = new ArrayList<>(1);
        easyTilePossibilityAsList.add(easyTile);
        allNeighboursNotInArea.remove(reverseDirection.get(direction));
        try {
            TilePropagationService.propagate(reverseDirection.get(direction), easyTilePossibilityAsList, potentialTile);
        } catch (MapGenerationException e) {
            return false;
        }
        for (Integer neighbourDirection : allNeighboursNotInArea.keySet()) {
            try {
                TilePropagationService.propagate(reverseDirection.get(neighbourDirection),
                        board.getTile(allNeighboursNotInArea.get(neighbourDirection)).getPossibleTileContentLeft(), potentialTile);
            } catch (MapGenerationException e) {
                return false;
            }
        }
        return true;
    }


    private static boolean couldBeGreenIfItWereNew(Board board, HashMap<Integer, PairOfCoordinates> allNeighboursNotInArea, int easyTile) {
        //TODO rename
        TileInProgress potentialTile = new TileInProgress();
        for (Integer direction : allNeighboursNotInArea.keySet()) {
            try {
                TilePropagationService.propagate(reverseDirection.get(direction),
                        board.getTile(allNeighboursNotInArea.get(direction)).getPossibleTileContentLeft(), potentialTile);
            } catch (MapGenerationException e) {
                return false;
            }
        }
        return potentialTile.getPossibleTileContentLeft().contains(easyTile);

    }

    private static void prefillQueue(PairOfCoordinates damageOrigin, Board board, ArrayList<PairOfCoordinates> patchArea, Queue<PairOfCoordinates> possibleBorderQueue) {
        int damageAreaBorderMinX = Math.max(damageOrigin.x() - DAMAGE_MIN_SIZE, 1);
        int damageAreaBorderMinY = Math.max(damageOrigin.y() - DAMAGE_MIN_SIZE, 1);
        int damageAreaBorderMaxX = Math.min(damageOrigin.x() + DAMAGE_MIN_SIZE + 1, board.getWidth());
        int damageAreaBorderMaxY = Math.min(damageOrigin.y() + DAMAGE_MIN_SIZE + 1, board.getHeight());

        for (int x = damageAreaBorderMinX; x < damageAreaBorderMaxX; x++) {
            for (int y = damageAreaBorderMinY; y < damageAreaBorderMaxY; y++) {
                patchArea.add(new PairOfCoordinates(x, y));
            }
        }
        for (int y = damageAreaBorderMinY; y < damageAreaBorderMaxY; y++) {
            possibleBorderQueue.add(new PairOfCoordinates(damageAreaBorderMinX, y));
            possibleBorderQueue.add(new PairOfCoordinates(damageAreaBorderMaxX - 1, y));
        }

        for (int x = damageAreaBorderMinX; x < damageAreaBorderMaxX; x++) {
            PairOfCoordinates xLow = new PairOfCoordinates(x, damageAreaBorderMinY);
            PairOfCoordinates xHigh = new PairOfCoordinates(x, damageAreaBorderMaxY - 1);
            if (!possibleBorderQueue.contains(xLow)) {
                possibleBorderQueue.add(xLow);
            }
            if (!possibleBorderQueue.contains(xHigh)) {
                possibleBorderQueue.add(xHigh);
            }
        }
    }

    private static HashMap<Integer, PairOfCoordinates> getAllNeighboursNotInArea(PairOfCoordinates centralTile, ArrayList<PairOfCoordinates> patchArea, Board board) {
        HashMap<Integer, PairOfCoordinates> directionToNeighbourMap = new HashMap<>(directionChanges.size());
        for (Integer direction : directionChanges.keySet()) {
            PairOfCoordinates neighbour = new PairOfCoordinates(
                    centralTile.x() + directionChanges.get(direction).x(),
                    centralTile.y() + directionChanges.get(direction).y());
            if (!(patchArea.contains(neighbour) || isOnBorder(neighbour, board) && isInBoard(neighbour, board))) {
                directionToNeighbourMap.put(direction, neighbour);
            }
        }
        return directionToNeighbourMap;
    }

    private static boolean isInBoard(PairOfCoordinates neighbour, Board board) {
        if (neighbour.y() < 0 || neighbour.x() < 0) return false;
        return neighbour.y() < board.getHeight() && neighbour.x() < board.getWidth();
    }

    private static void putAllNeighboursInQueueIfNotPresent(PairOfCoordinates possibleQueueMember, Queue<PairOfCoordinates>
            patchBorder, ArrayList<PairOfCoordinates> patchArea, Board board) {
        for (Integer direction : directionChanges.keySet()) {
            PairOfCoordinates neighbour = new PairOfCoordinates(
                    possibleQueueMember.x() + directionChanges.get(direction).x(),
                    possibleQueueMember.y() + directionChanges.get(direction).y());
            if (!(patchBorder.contains(neighbour) || patchArea.contains(neighbour) || isOnBorder(neighbour, board))) {
                patchBorder.add(neighbour);
            }
        }
    }

    private static boolean isOnBorder(PairOfCoordinates possibleBorderMember, Board board) {
        return possibleBorderMember.x() == 0 || possibleBorderMember.y() == 0 ||
                possibleBorderMember.x() == board.getWidth() - 1 || possibleBorderMember.y() == board.getHeight() - 1;
    }
}
