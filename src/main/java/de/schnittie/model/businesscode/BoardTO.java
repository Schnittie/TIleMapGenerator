package de.schnittie.model.businesscode;

import java.util.HashMap;

public class BoardTO {
    private int[][] boardOfIDs;

    private int WIDTH; //x
    private int HEIGHT;//y
    private HashMap<Integer, String> filePathMap;
    public BoardTO(int[][] boardOfIDs, int WIDTH, int HEIGHT, HashMap<Integer, String> filePathMap) {
        this.boardOfIDs = boardOfIDs;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.filePathMap = filePathMap;
    }
    public BoardTO setFilePathMap(HashMap<Integer, String> filePathMap) {
        this.filePathMap = filePathMap;
        return this;
    }
    public int getIDat(int x, int y){
        return boardOfIDs[x][y];
    }
    public int getWIDTH() {
        return WIDTH;
    }
    public int getHEIGHT() {
        return HEIGHT;
    }
    public HashMap<Integer, String> getFilePathMap() {
        return filePathMap;
    }
}
