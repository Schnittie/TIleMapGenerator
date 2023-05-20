package de.schnittie.model.fillingDB;

import de.schnittie.model.database.DBinteractions;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileCreation {
    private static final DBinteractions dbinteractions = DBinteractions.getInstance();
    private static final String TILEFOLDER = DBinteractions.getTILEFOLDER();

    public static void putTilesInDB(File inputFile, boolean shouldRotate) {
        try {
            splitImage(inputFile,shouldRotate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File folder = new File(TILEFOLDER);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                String imageUrl = file.getName();
                dbinteractions.putTileIntoDB(imageUrl);
            }
        }
    }

    private static void splitImage(File inputFile, boolean shouldRotate) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputFile);

        int numCols = inputImage.getWidth() / 27;
        int numRows = inputImage.getHeight() / 27;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * 27;
                int y = row * 27;

                BufferedImage outputImage = inputImage.getSubimage(x, y, 27, 27);

                // Check if all pixels in the subimage are transparent
                if (isSubimageTransparent(outputImage)) {
                    continue;  // Skip empty (transparent) subimages
                }
                String rotateFileName = shouldRotate ? "rotated" : "stern";
                String baseOutputFilePath = TILEFOLDER + rotateFileName + "_" + row + "_" + col + "_";

                // Save the original subimage
                String outputFilePath = baseOutputFilePath + "0.png";
                ImageIO.write(outputImage, "png", new File(outputFilePath));

                if (shouldRotate) {
                    // Save three additional rotated versions of the subimage
                    for (int rotation = 1; rotation <= 3; rotation++) {
                        outputImage = rotateImage(outputImage, 90);  // Rotate the image by 90 degrees
                        outputFilePath = baseOutputFilePath + rotation + ".png";
                        ImageIO.write(outputImage, "png", new File(outputFilePath));
                    }
                }
            }
        }
    }
    private static BufferedImage rotateImage(BufferedImage image, int degrees) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(degrees), image.getWidth() / 2.0, image.getHeight() / 2.0);

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
