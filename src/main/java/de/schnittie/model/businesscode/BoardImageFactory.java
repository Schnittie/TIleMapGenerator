package de.schnittie.model.businesscode;

import de.schnittie.model.database.DBinteractions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BoardImageFactory {

    private static final int TILE_SIZE = 15;
    private static BufferedImage DEFAULT = null; //The default image should only ever be shown if something goes wrong

    static {
        try {
            DEFAULT = ImageIO.read(new File(DBinteractions.getDbFolder() + File.separator + "default.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final DBinteractions dBinteractions = DBinteractions.getInstance();
    private static final HashMap<Integer, String> filePathMap = dBinteractions.getFilePathMap();

    public static BufferedImage generateBoardImage(Tile[][] board, String outputFilePath) {
        int width = board.length;
        int height = board[0].length;
        int imageWidth = width * TILE_SIZE;
        int imageHeight = height * TILE_SIZE;

        BufferedImage boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = boardImage.createGraphics();
        for (int col = 0; col < height; col++) {
            for (int row = 0; row < width; row++) {
                Tile tile = board[row][col];

                BufferedImage tileImage = null;
                try {
                    if (tile.getContent() != -1) {
                        tileImage = ImageIO.read(new File(filePathMap.get(tile.getContent())));
                    } else {
                        tileImage = DEFAULT;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                graphics2D.drawImage(tileImage, row * TILE_SIZE, col * TILE_SIZE, null);
            }
        }

        graphics2D.dispose();

        try {
            ImageIO.write(boardImage, "png", new File(outputFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boardImage;
    }
}

