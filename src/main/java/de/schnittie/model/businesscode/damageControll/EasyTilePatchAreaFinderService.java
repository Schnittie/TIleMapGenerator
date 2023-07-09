package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.database.DBinteractions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class EasyTilePatchAreaFinderService {
    public static PatchInstruction getPatchInstruction(PairOfCoordinates damageOrigin, ArrayList<ArrayList<Integer>> easyTileLists, Board board) {
        PatchInstruction patchInstruction;
        ArrayList<Integer> easyTileSetExamples = new ArrayList<>(easyTileLists.size());
        for (ArrayList<Integer> easyTileList : easyTileLists) {
            easyTileSetExamples.add(easyTileList.get(0));
        }
        for (Integer easyTileExampleId : easyTileSetExamples) {
            patchInstruction = tryToFindPatchArea(damageOrigin, easyTileExampleId, board);
            if (patchInstruction != null) return patchInstruction;
        }
        throw new RuntimeException("No easy Patch for Damage found");

    }

    private static PatchInstruction tryToFindPatchArea(PairOfCoordinates damageOrigin, int easyTileExampleId, Board board) {
        ArrayList<PairOfCoordinates> patchArea = new ArrayList<>();
        ArrayList<PairOfCoordinates> patchBorder = new ArrayList<>();
        Queue<PairOfCoordinates> possibleBorderQueue = new LinkedList<>();
        possibleBorderQueue.add(damageOrigin);
        while (!possibleBorderQueue.isEmpty()) {
            PairOfCoordinates possibleBorderMember = possibleBorderQueue.poll();
            patchArea.add(possibleBorderMember);
            if (isOnBorder(possibleBorderMember, board)) {
                return null;
            }
            if (board.getTile(possibleBorderMember).getPossibleTileContentLeft().contains(easyTileExampleId)) {
                patchBorder.add(possibleBorderMember);
                continue;
            }
            putAllNeighboursInQueueIfNotPresent(damageOrigin, possibleBorderQueue);
        }
        return new PatchInstruction(patchArea, patchBorder, easyTileExampleId);
    }

    private static void putAllNeighboursInQueueIfNotPresent(PairOfCoordinates damageOrigin, Queue<PairOfCoordinates> patchBorder) {
        HashMap<Integer, PairOfCoordinates> directionChanges = DBinteractions.getInstance().getDirectionChanges();
        for (Integer direction :
                directionChanges.keySet()) {
            PairOfCoordinates neighbour = new PairOfCoordinates(
                    damageOrigin.x() + directionChanges.get(direction).x(),
                    damageOrigin.y() + directionChanges.get(direction).y());
            if (!patchBorder.contains(neighbour)) {
                patchBorder.add(neighbour);
            }
        }
    }

    private static boolean isOnBorder(PairOfCoordinates possibleBorderMember, Board board) {
        return possibleBorderMember.x() == 0 || possibleBorderMember.y() == 0 ||
                possibleBorderMember.x() == board.getWidth() - 1 || possibleBorderMember.y() == board.getHeight() - 1;
    }
}
