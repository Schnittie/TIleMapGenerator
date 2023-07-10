package de.schnittie.model.businesscode.board.splitting;
//TODO
// Don't extend Throwable, unless you know what you do.
// this is an exception and should be at least extend Exception!
// In case you want a checked exception, go for "extends Exception" or "extends RuntimeException" otherwise.
// Plus: this would save you ALL the code inside this class, since both have
// the message- and cause concept already implemented!
public class BoardFusionException extends Throwable {
    private final String message;
    public BoardFusionException(String cause) {
        this.message = cause;
    }
    public String getMessage(){
        return message;
    }
}
