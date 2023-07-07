package de.schnittie.model.businesscode.board.splitting;

public class BoardFusionException extends Throwable {
    private final String message;
    public BoardFusionException(String cause) {
        this.message = cause;
    }
    public String getMessage(){
        return message;
    }
}
