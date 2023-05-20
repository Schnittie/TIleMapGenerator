package de.schnittie.model.fillingDB;

import de.schnittie.model.database.DBinteractions;

public class DirectionCreation {
    private final static DBinteractions dBinteractions = DBinteractions.getInstance();

    public static void putDirectionsIntoDB() {
        dBinteractions.putDirectionInDB(0, "Above", 0, -1,2);
        dBinteractions.putDirectionInDB(1, "Right", 1, 0,3);
        dBinteractions.putDirectionInDB(2, "Below", 0, 1,0);
        dBinteractions.putDirectionInDB(3, "Left", -1, 0,1);
    }
}
