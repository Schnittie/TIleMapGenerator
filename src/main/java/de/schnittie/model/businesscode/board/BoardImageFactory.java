package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.TileImageLoaderService;
import de.schnittie.model.database.DBinteractions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BoardImageFactory {

    private static final int TILE_SIZE = 15;

    public static BufferedImage renderBoardImage(BoardTO board) {
        System.out.println("Rendering Image...");
        int width = board.getWIDTH();
        int height = board.getHEIGHT();
        int imageWidth = width * TILE_SIZE;
        int imageHeight = height * TILE_SIZE;
        HashMap<Integer, BufferedImage> imageById = TileImageLoaderService.getImageMapFromFilepathMap();
        BufferedImage boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = boardImage.createGraphics();
        for (int colum = 0; colum < height; colum++) {
            for (int row = 0; row < width; row++) {
                int tileID = board.getIDat(row, colum);
                graphics2D.drawImage(imageById.get(tileID), row * TILE_SIZE, colum * TILE_SIZE, null);
            }
        }

        graphics2D.dispose();

        try {
            ImageIO.write(boardImage, "png", new File(
                    DBinteractions.getInstance().getDbFolder() + File.separator + "generatedMapRender.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boardImage;
    }


}

