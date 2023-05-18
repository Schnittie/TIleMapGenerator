package de.schnittie.database;

public record RuleCreationTO(int this_Tile, int next_to, int that_Tile, String this_TileFilepath, String that_TileFilepath) {
}
