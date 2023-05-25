package de.schnittie.model.fillingDB;

import de.schnittie.model.database.DBinteractions;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TileCreation {
    private static final int TILE_SIZE = 15;
    private static final DBinteractions dbinteractions = DBinteractions.getInstance();
    private static final String TILEFOLDER = DBinteractions.getTILEFOLDER();

    public static void addTiles(HashMap<File, Integer> tilemapMap) {
        for (File tilemap : tilemapMap.keySet()) {
            try {
                TileCreation.splitImage(tilemap, tilemapMap.get(tilemap));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        File folder = new File(TILEFOLDER);
        File[] files = folder.listFiles();

        putTilesInDB(files);
        RuleCreation.generateRules();
    }

    private static void putTilesInDB(File[] files) {
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                String imageUrl = file.getName();
                dbinteractions.putTileIntoDB(imageUrl);
            }
        }
    }

    private static void splitImage(File inputFile, int shouldRotate) throws IOException {
        if (shouldRotate < 0 || shouldRotate > 3) {
            shouldRotate = 0;
        }
        BufferedImage inputImage = ImageIO.read(inputFile);

        int numCols = inputImage.getWidth() / TILE_SIZE;
        int numRows = inputImage.getHeight() / TILE_SIZE;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;

                BufferedImage outputImage = inputImage.getSubimage(x, y, TILE_SIZE, TILE_SIZE);

                // Check if all pixels in the subimage are transparent
                if (isSubimageTransparent(outputImage)) {
                    continue;  // Skip empty (transparent) subimages
                }
                String rotateFileName = "rotated_" + shouldRotate +"_times";
                String baseOutputFilePath = TILEFOLDER + rotateFileName + "_" + row + "_" + col + "_";

                // Save the original subimage
                String outputFilePath = baseOutputFilePath + "0.png";
                ImageIO.write(outputImage, "png", new File(outputFilePath));


                    for (int rotation = 1; rotation <= shouldRotate; rotation++) {
                        outputImage = rotateImage(outputImage);  // Rotate the image by 90 degrees
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
