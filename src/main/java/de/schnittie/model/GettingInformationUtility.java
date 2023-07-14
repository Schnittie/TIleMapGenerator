package de.schnittie.model;

import de.schnittie.model.logic.TileImageLoaderUtility;
import de.schnittie.model.database.DBinteractions;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GettingInformationUtility {
    private static DBinteractions dBinteractions = DBinteractions.getInstance();
    public static HashMap<Integer, Integer> getTileIdToProbabilityMap(){
        return dBinteractions.getProbabilityMap();
    }
    public static HashMap<Integer, BufferedImage> getTileIdToBufferedImageMap(){
       return TileImageLoaderUtility.getImageMapFromFilepathMap();
    }
}
