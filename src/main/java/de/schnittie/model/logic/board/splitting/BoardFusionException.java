package de.schnittie.model.logic.board.splitting;

public class BoardFusionException extends Exception {
    private final String message;
    public BoardFusionException(String cause) {
        this.message = cause;
    }
    public String getMessage(){
        return message;
    }
}
