package de.schnittie.model.logic.board;

import de.schnittie.model.logic.TileImageLoaderUtility;
import de.schnittie.model.database.DBinteractions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BoardImageFactory {

    private static final int TILE_SIZE = 15;

    public static BufferedImage renderBoardImage(BoardTO board){
       return renderBoardImage(board, "generatedMapRender.png");
    }
    private static BufferedImage renderBoardImage(BoardTO board,String filename) {
        System.out.println("Rendering Image...");
        int width = board.getWIDTH();
        int height = board.getHEIGHT();
        int imageWidth = width * TILE_SIZE;
        int imageHeight = height * TILE_SIZE;
        HashMap<Integer, BufferedImage> imageById = TileImageLoaderUtility.getImageMapFromFilepathMap();
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
                    DBinteractions.getInstance().getDbFolder() + File.separator + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boardImage;
    }

    public static void renderDamageImage(BoardTO board, PairOfCoordinates coordinatesOfDamage){
            board.setIDat(coordinatesOfDamage.x(), coordinatesOfDamage.y(), -2);
            renderBoardImage(board, coordinatesOfDamage.x() + "_" + coordinatesOfDamage.y() + "_generatedDamageRender.png");
    }
}

