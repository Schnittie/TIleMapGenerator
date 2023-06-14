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
    public static BufferedImage generateBoardImage(BoardTO board) {
        int width = board.getWIDTH();
        int height = board.getHEIGHT();
        int imageWidth = width * TILE_SIZE;
        int imageHeight = height * TILE_SIZE;
        HashMap<Integer, String> filePathMap = board.getFilePathMap();

        BufferedImage boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = boardImage.createGraphics();
        for (int colum = 0; colum < height; colum++) {
            for (int row = 0; row < width; row++) {
                int tileID = board.getIDat(row, colum);

                BufferedImage tileImage = null;
                try {
                    if (tileID != -1) {
                        tileImage = ImageIO.read(new File(filePathMap.get(tileID)));
                    } else {
                        tileImage = DEFAULT;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                graphics2D.drawImage(tileImage, row * TILE_SIZE, colum * TILE_SIZE, null);
            }
        }

        graphics2D.dispose();

        try {
            ImageIO.write(boardImage, "png", new File(
                    DBinteractions.getDbFolder() + File.separator + "generatedMapRender.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boardImage;
    }
}

