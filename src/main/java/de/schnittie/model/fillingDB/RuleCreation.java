package de.schnittie.model.fillingDB;

import de.schnittie.model.database.DBinteractions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RuleCreation {
    private static DBinteractions dBinteractions = DBinteractions.getInstance();
    private static HashMap<Integer, ArrayList<Integer>> tileSocketList = new HashMap<>();
    private static ArrayList<Socket> socketSet = new ArrayList<>();
    private static ArrayList<Integer> colourSet = new ArrayList<>();
    private static final HashMap<Integer, Integer> directionChanges = dBinteractions.getReverseDirection();
    private static final int TOLERANCE = 100;

    public static void generateRules() {
        for (Integer tileId : dBinteractions.getPossibleTileIDs(dBinteractions.getNumberOfTiles())) {
            try {
                BufferedImage tileImage = ImageIO.read(new File(dBinteractions.getFilePath(tileId)));
                putTileIntoLists(tileImage, tileId);
            } catch (IOException e) {
                System.out.println("error reading file for " + tileId);
                throw new RuntimeException(e);
            }
        }
        for (Integer this_tile_Id : tileSocketList.keySet()) {
            for (Integer direction : directionChanges.keySet()) {
                for (Integer that_tile_Id : tileSocketList.keySet()) {
                    if (tileSocketList.get(this_tile_Id).get(direction).equals
                            (tileSocketList.get(that_tile_Id).get(directionChanges.get(direction)))) {
                        dBinteractions.putRuleIntoDB(this_tile_Id, that_tile_Id, directionChanges.get(direction));
                    }
                }
            }
        }

    }

    private static void putTileIntoLists(BufferedImage tileImage, Integer tileId) {
        ArrayList<Integer> sockets = new ArrayList<>(4);
        sockets.add(getSocketId(tileImage, 4, 0, 13, 0, 22, 0));
        sockets.add(getSocketId(tileImage, 26, 4, 26, 13, 26, 22));
        sockets.add(getSocketId(tileImage, 4, 26, 13, 26, 22, 26));
        sockets.add(getSocketId(tileImage, 0, 4, 0, 13, 0, 22));
        tileSocketList.put(tileId, sockets);
    }

    private static int getSocketId(BufferedImage tileImage, int x1, int y1, int x2, int y2, int x3, int y3) {
        int[] colours = new int[3];
        colours[0] = getColourId(tileImage, x1, y1);
        colours[1] = getColourId(tileImage, x2, y2);
        colours[2] = getColourId(tileImage, x3, y3);

        Socket socket = new Socket(colours);
        if (!socketSet.contains(socket)) {
            socketSet.add(socket);
        }
        return socketSet.indexOf(socket);
    }


    private static int getColourId(BufferedImage tileImage, int x, int y) {
        int colour = tileImage.getRGB(x, y);
        int colourId = colourIsInSet(colour);
        if (colourId == -1) {
            colourSet.add(colour);
            return colourSet.indexOf(colour);
        }
        return colourId;
    }

    private static int colourIsInSet(int colour) {
        for (int colourFromSet : colourSet) {
            int redDiff = Math.abs((colour >> 16) & 0xFF - (colourFromSet >> 16) & 0xFF);
            int greenDiff = Math.abs((colour >> 8) & 0xFF - (colourFromSet >> 8) & 0xFF);
            int blueDiff = Math.abs((colour & 0xFF) - (colourFromSet & 0xFF));
            if (redDiff + greenDiff + blueDiff <= TOLERANCE) {
                return colourSet.indexOf(colourFromSet);
            }
        }
        return -1;
    }
}
