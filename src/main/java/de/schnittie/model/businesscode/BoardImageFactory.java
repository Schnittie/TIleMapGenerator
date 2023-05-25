package de.schnittie.model.businesscode;

import de.schnittie.model.database.DBinteractions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoardImageFactory {

    private static final int TILE_SIZE = 15;
    private static BufferedImage DEFAULT = null;
    static {
        // this way you won't need to read the image on every darn iteration in your for-loop below.
        try {
            DEFAULT = ImageIO.read(new File(DBinteractions.getDbFolder() + File.separator + "default.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static DBinteractions dBinteractions = DBinteractions.getInstance();

    public static BufferedImage generateBoardImage(Tile[][] board, String outputFilePath, int height, int width) {
        int imageWidth = width * TILE_SIZE;
        int imageHeight = height * TILE_SIZE;

        BufferedImage boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = boardImage.createGraphics();
        for (int col = 0; col < height; col++) {
            for (int row = 0; row < width; row++) {
                Tile tile = board[row][col];

                BufferedImage tileImage = null;
                try {
                    if (tile.getContent() != -1) {
                        tileImage = ImageIO.read(new File(dBinteractions.getFilePath(tile.getContent())));
                    } else {
                        tileImage = DEFAULT;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2d.drawImage(tileImage, row * TILE_SIZE, col * TILE_SIZE, null);
            }
        }

        g2d.dispose();

        try {
            ImageIO.write(boardImage, "png", new File(outputFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boardImage;
    }
}

