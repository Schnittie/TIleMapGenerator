package de.schnittie.model.database.fillingDB;

import de.schnittie.model.database.DBinteractions;
import de.schnittie.model.database.RuleTO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RuleCreation {
    private static final DBinteractions dBinteractions = DBinteractions.getInstance();
    private static final ArrayList<Socket> socketSet = new ArrayList<>();
    private static final ArrayList<Integer> colourSet = new ArrayList<>();
    private static final int TOLERANCE = 5;

    public static void generateRules() {
        //Each side of a TileInProgress has a Socket
        //the Sockets are based on Pixel colours
        //if the Socket of a TileInProgress matches with the Socket of another TileInProgress on the reverse Side it fits
        //a socket matches with another if it is the same (comparison by id)

        DirectionCreation.putDirectionsIntoDB();
        HashMap<Integer, Integer> directionChanges = dBinteractions.getReverseDirection();
        HashMap<Integer, String> tilePathMap = dBinteractions.getFilePathMap();
        HashMap<Integer, ArrayList<Integer>> tileSocketList = new HashMap<>();

        for (Integer tileId : dBinteractions.getPossibleTileIDs()) {
            try {
                BufferedImage tileImage = ImageIO.read(new File(tilePathMap.get(tileId)));
                putTileIntoLists(tileImage, tileId, tileSocketList);
            } catch (IOException e) {
                System.out.println("error reading file for " + tileId);
                throw new RuntimeException(e);
            }
        }
        ArrayList<RuleTO> listOfRulesToCreate = new ArrayList<>();
        for (Integer this_tile_Id : tileSocketList.keySet()) {
            for (Integer direction : directionChanges.keySet()) {
                for (Integer that_tile_Id : tileSocketList.keySet()) {

                    int neighbourValidity = AdjacencyByFilePathHelperUtility.extraRuleValidly(this_tile_Id, that_tile_Id,
                            direction, tilePathMap);

                    if (neighbourValidity == 1 || (tileSocketList.get(this_tile_Id).get(direction).equals
                            (tileSocketList.get(that_tile_Id).get(directionChanges.get(direction))))&& neighbourValidity==0) {
                        listOfRulesToCreate.add(new RuleTO(-1, this_tile_Id, that_tile_Id, directionChanges.get(direction)));
                    }
                }
            }
        }
        try {
            RuleValidationUtility.areRulesValid(listOfRulesToCreate);
        } catch (InvalidAdjacencyException e) {
            e.printFaultyTiles();
            throw new RuntimeException(e);
        }
        System.out.println("putting " + listOfRulesToCreate.size() + " Entries into the DB...");
        dBinteractions.putListOfRulesIntoDB(listOfRulesToCreate);

        EasyTileCreationUtility.createEasyTiles(tileSocketList, socketSet, tilePathMap);
    }

    private static void putTileIntoLists(BufferedImage tileImage, Integer tileId,
                                         HashMap<Integer, ArrayList<Integer>> tileSocketList ) {
        //We save compare Sockets by ID
        ArrayList<Integer> sockets = new ArrayList<>(4);
        sockets.add(getSocketId(tileImage, 2, 0, 7, 0, 12, 0));
        sockets.add(getSocketId(tileImage, 14, 2, 14, 7, 14, 12));
        sockets.add(getSocketId(tileImage, 2, 14, 7, 14, 12, 14));
        sockets.add(getSocketId(tileImage, 0, 2, 0, 7, 0, 12));
        tileSocketList.put(tileId, sockets);
    }

    private static int getSocketId(BufferedImage tileImage, int x1, int y1, int x2, int y2, int x3, int y3) {
        //if there is no Socket, with the colours, a new one is created
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
        //like Sockets, we compare colours by ID not by the values
        int colour = tileImage.getRGB(x, y);
        int colourId = colourIsInSet(colour);
        if (colourId == -1) {
            colourSet.add(colour);
            return colourSet.indexOf(colour);
        }
        return colourId;
    }

    private static int colourIsInSet(int colour) {
        //because colours are weird we grant a Tolerance of how much a Colour can be different before it's a new colour
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
