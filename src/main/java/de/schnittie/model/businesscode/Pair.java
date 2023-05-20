package de.schnittie.model.businesscode;

public record Pair(int x, int y) {
    @Override
    public String toString() {
        return (x + " " + y);
    }
}
