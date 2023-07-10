package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.businesscode.tile.tileObjects.TileInProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// util?
public class EasyTilePatchAreaFinderService {
    private static final int DAMAGE_MIN_SIZE = 1;
    private static final HashMap<Integer, PairOfCoordinates> directionChanges = Configuration.getInstance().getDirectionChanges();

    public static PatchInstruction getPatchInstruction(PairOfCoordinates damageOrigin, ArrayList<ArrayList<Integer>> easyTileLists, Board board) {
        PatchInstruction patchInstruction;
        ArrayList<Integer> easyTileSetExamples = new ArrayList<>(easyTileLists.size());
        for (ArrayList<Integer> easyTileList : easyTileLists) {
            easyTileSetExamples.add(easyTileList.get(0));
        }
        // all these printlns should probably be replaced by a logger... its fine for development, but you'll like
        // not to bother the users with pointless logging stuff they'll never see anyways while
        // eating CPU-Cycles like they were crisps. A logger can be configured to shut up for prod builds.
        // this applies to every single println in your code.
        System.out.println("Trying to make out a damage Area...");
        for (Integer easyTileExampleId : easyTileSetExamples) {
            patchInstruction = tryToFindPatchArea(damageOrigin, easyTileExampleId, board);
            if (patchInstruction != null) return patchInstruction;
        }
        System.out.println(damageOrigin.x() + damageOrigin.y());
        throw new RuntimeException("No easy Patch for Damage found");
    }

    private static PatchInstruction tryToFindPatchArea(PairOfCoordinates damageOrigin, int easyTileExampleId, Board board) {
        ArrayList<PairOfCoordinates> patchArea = new ArrayList<>();
        Queue<PairOfCoordinates> possibleBorderQueue = new LinkedList<>();
        board.setTile(damageOrigin, new TileInProgress());

        prefillQueue(damageOrigin, board, patchArea, possibleBorderQueue);

        possibleBorderQueue.add(damageOrigin);
        while (!possibleBorderQueue.isEmpty()) {
            if (possibleBorderQueue.size() > 50) {
                System.out.println("Abandon hope for tile " + easyTileExampleId);
                return null;
            }
            PairOfCoordinates possibleBorderMember = possibleBorderQueue.poll();
            if (isOnBorder(possibleBorderMember, board)) {
                System.out.println("No valid Area found for tile " + easyTileExampleId);
                return null;
            }
            if (!patchArea.contains(possibleBorderMember)){
                patchArea.add(possibleBorderMember);
            }
            if (board.getTile(possibleBorderMember).getPossibleTileContentLeft().contains(easyTileExampleId)) {
                continue;
            }
            putAllNeighboursInQueueIfNotPresent(possibleBorderMember, possibleBorderQueue, patchArea);
        }
        System.out.println("Damage area found for " + easyTileExampleId);
        return new PatchInstruction(patchArea, easyTileExampleId);
    }

    private static void prefillQueue(PairOfCoordinates damageOrigin, Board board, ArrayList<PairOfCoordinates> patchArea, Queue<PairOfCoordinates> possibleBorderQueue) {
        int damageAreaBorderMinX = Math.max(damageOrigin.x() - DAMAGE_MIN_SIZE, 1);
        int damageAreaBorderMinY = Math.max(damageOrigin.y() - DAMAGE_MIN_SIZE, 1);
        int damageAreaBorderMaxX = Math.min(damageOrigin.x() + DAMAGE_MIN_SIZE+1, board.getWidth());
        int damageAreaBorderMaxY = Math.min(damageOrigin.y() + DAMAGE_MIN_SIZE+1, board.getHeight());

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
            if (!possibleBorderQueue.contains(xLow)){
                possibleBorderQueue.add(xLow);
            }
            if (!possibleBorderQueue.contains(xHigh)){
                possibleBorderQueue.add(xHigh);
            }

        }
    }

    private static void putAllNeighboursInQueueIfNotPresent(PairOfCoordinates damageOrigin, Queue<PairOfCoordinates>
            patchBorder, ArrayList<PairOfCoordinates> patchArea) {
        for (Integer direction : directionChanges.keySet()) {
            PairOfCoordinates neighbour = new PairOfCoordinates(
                    damageOrigin.x() + directionChanges.get(direction).x(),
                    damageOrigin.y() + directionChanges.get(direction).y());
            if (!(patchBorder.contains(neighbour) || patchArea.contains(neighbour))) {
                patchBorder.add(neighbour);
            }
        }
    }

    private static boolean isOnBorder(PairOfCoordinates possibleBorderMember, Board board) {
        return possibleBorderMember.x() == 0 || possibleBorderMember.y() == 0 ||
                possibleBorderMember.x() == board.getWidth() - 1 || possibleBorderMember.y() == board.getHeight() - 1;
    }
}
