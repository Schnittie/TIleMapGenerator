import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoardImageGenerator {

    private static final int TILE_SIZE = 16;

    public static void generateBoardImage(Tile[][] board, String outputFilePath, int height, int width) {
        int imageWidth = width * TILE_SIZE;
        int imageHeight = height * TILE_SIZE;

        BufferedImage boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = boardImage.createGraphics();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Tile tile = board[row][col];
                String tileImagePath = tile.getContent().getImagePath();
                BufferedImage tileImage = null;
                try {
                    tileImage = ImageIO.read(new File(tileImagePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2d.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE, null);
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

