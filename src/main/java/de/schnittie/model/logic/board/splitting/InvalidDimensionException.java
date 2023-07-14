package de.schnittie.model.logic.board.splitting;
public class InvalidDimensionException extends Exception {
    private final String message;
    public InvalidDimensionException(String cause) {
        this.message = cause;
    }
    public String getMessage(){
        return message;
    }
}
