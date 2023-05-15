package BusinessCode;

import DataBase.DBinteractions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoardImageGenerator {

    private static final int TILE_SIZE = 16;
    private static final String DEFAULT = "C:\\Users\\laure\\Documents\\Dev\\LegoBattlesMapGenerator\\src\\main\\DefaultImages\\default.png";
    private static DBinteractions dBinteractions = DBinteractions.getInstance();

    public static void generateBoardImage(Tile[][] board, String outputFilePath, int height, int width) {
        int imageWidth = width * TILE_SIZE;
        int imageHeight = height * TILE_SIZE;

        BufferedImage boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = boardImage.createGraphics();
        String tileImagePath = DEFAULT;
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                Tile tile = board[row][col];
                if (tile.getContent() != -1) {
                    tileImagePath = dBinteractions.getFilePath(tile.getContent());
                } else {
                    tileImagePath = DEFAULT;
                }
                BufferedImage tileImage = null;
                try {
                    tileImage = ImageIO.read(new File(tileImagePath));
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
    }
}

