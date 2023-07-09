package de.schnittie.model.businesscode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TileImageLoaderService {
    public static HashMap<Integer, BufferedImage> getImageMapFromFilepathMap() {
        HashMap<Integer, String> filePathMap = Configuration.getInstance().getFilePathMap();
        HashMap<Integer, BufferedImage> imageById = new HashMap<>(filePathMap.size() + 1);
        try {
            for (Integer id : filePathMap.keySet()) {
                imageById.put(id, ImageIO.read(new File(filePathMap.get(id))));
            }
            BufferedImage DEFAULT = null; //The default image should only ever be shown if something goes wrong
            BufferedImage DAMAGE = null; //The default image should only ever be shown if something goes wrong
            try {
                DEFAULT = ImageIO.read(new File(Configuration.getInstance().getDbFolder() + File.separator + "default.png"));
                DAMAGE = ImageIO.read(new File(Configuration.getInstance().getDbFolder() + File.separator + "damage.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageById.put(-1, DEFAULT);
            imageById.put(-2, DAMAGE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageById;
    }
}
