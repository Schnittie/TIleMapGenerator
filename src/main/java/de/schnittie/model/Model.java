package de.schnittie.model;

import de.schnittie.model.businesscode.Board;
import de.schnittie.model.businesscode.BoardImageFactory;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.fillingDB.TileCreation;
import de.schnittie.view.ModelListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {

    private List<ModelListener> listeners = new ArrayList<>();
    private Board board;
    private HashMap<File, Integer> tilesToAdd = new HashMap<>();
    private BufferedImage lastMap;

    public BufferedImage generateMap(int width, int height) {
        try {
            board = new Board(width, height);
            board.fill();
        } catch (MapGenerationException e) {
            notifyListeners(new GenerationErrorEvent("An Error occurred"));
        }
        lastMap = BoardImageFactory.generateBoardImage(board.getBoard(), "generatedMapRender.png");
        notifyListeners(new NewMapEvent(lastMap));
        return lastMap;
    }
    private void notifyListeners(MapGeneratorEvent event) {
        for (ModelListener listener : listeners) {
            listener.update(event);
        }
    }
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }

    public void addTiles(String filepath, int rotation) {
        tilesToAdd.put(new File(filepath), rotation);
    }

    public void generateRules() {
        TileCreation.addTiles(tilesToAdd);
    }

    public BufferedImage getLastMap() {
        return lastMap;
    }
}
