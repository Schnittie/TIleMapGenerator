package de.schnittie.model.database.fillingDB;

import de.schnittie.model.database.DBinteractions;
import de.schnittie.model.database.configurations.RuleCreationInstruction;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
//TODO
// ..util.
public class TileCreation {
    private static final int TILE_SIZE = 15;
    private static final DBinteractions dbinteractions = DBinteractions.getInstance();

    public static void addTiles(HashMap<InputStream, RuleCreationInstruction> tilemapToRotationInstructionMap, String tileFolder, HashMap<InputStream, String> inputStreamToFilenameMap) {
        //receives a Map that mapps a file to how often it should rotate

        for (InputStream inputStream : tilemapToRotationInstructionMap.keySet()) {
            try {
                TileCreation.splitImage(inputStream, tilemapToRotationInstructionMap.get(inputStream).getRotationInteger(), tileFolder, inputStreamToFilenameMap.get(inputStream));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        File folder = new File(tileFolder);
        File[] files = folder.listFiles();

        if (files == null) {
            throw new RuntimeException("files not found");
        }
        putTilesInDB(files);
        RuleCreation.generateRules();
    }

    private static void putTilesInDB(File[] files) {
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                String fileName = file.getName();
                dbinteractions.putTileIntoDB(fileName, getProbabilityForTile(fileName) * 10);
            }
        }
    }

    private static int getProbabilityForTile(String filename) {
        try {
            ArrayList<Integer> numbersInUrl = ExtractingNumberUtility.extractNumbers(filename);
            if (filename.contains("Neighbour")) {
                return numbersInUrl.get(0);
            }
            return 4 - numbersInUrl.get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Couldn't parse probability from TileName");
            return 1;
        }
    }

    private static void splitImage(InputStream inputStream, int shouldRotate, String tileFolder, String filename) throws IOException {
        //splits a Tilemap into the individual images and rotates it the specified amount, then saves it
        BufferedImage inputImage = ImageIO.read(inputStream);

        int numCols = inputImage.getWidth() / TILE_SIZE;
        int numRows = inputImage.getHeight() / TILE_SIZE;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;

                BufferedImage outputImage = inputImage.getSubimage(x, y, TILE_SIZE, TILE_SIZE);


                if (isSubimageTransparent(outputImage)) {
                    continue;  // Skip empty (transparent) sub-images
                }
                if (shouldRotate < 0) {
                    StringBuilder edgeMarker = new StringBuilder();
                    if (row == numRows - 1) {
                        edgeMarker.append("Below");
                    }
                    if (row == 0) {
                        edgeMarker.append("Above");
                    }
                    if (col == numCols - 1) {
                        edgeMarker.append("Right");
                    }
                    if (col == 0) {
                        edgeMarker.append("Left");
                    }
                    String baseOutputFilePath = tileFolder + "NeighbourRules Based on " + filename + " p" + shouldRotate + edgeMarker + "_" + col + "_" + row + ".png";

                    ImageIO.write(outputImage, "png", new File(baseOutputFilePath));
                    continue;
                }
                String rotateFileName = "rotated_" + shouldRotate + "_times_from_" + filename;
                String baseOutputFilePath = tileFolder + rotateFileName + "_" + col + "_" + row + "_";


                // Save the original subimage
                String outputFilePath = baseOutputFilePath + "0.png";
                ImageIO.write(outputImage, "png", new File(outputFilePath));


                for (int rotation = 1; rotation <= shouldRotate; rotation++) {
                    // Rotate the image by 90 degrees for
                    outputImage = rotateImage(outputImage);
                    outputFilePath = baseOutputFilePath + rotation + ".png";
                    ImageIO.write(outputImage, "png", new File(outputFilePath));
                }

            }
        }
    }

    private static BufferedImage rotateImage(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(90), image.getWidth() / 2.0, image.getHeight() / 2.0);

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    private static boolean isSubimageTransparent(BufferedImage image) {
        // Check if all pixels in the subimage are transparent
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                if ((pixel >> 24) != 0x00) {
                    return false;
                }
            }
        }

        return true;
    }
}
