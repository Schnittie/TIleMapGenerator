package de.schnittie.model.mvcStuffs;

public class GenerationErrorEvent extends MapGeneratorEvent {
    private final String errorMessage;

    public GenerationErrorEvent(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
