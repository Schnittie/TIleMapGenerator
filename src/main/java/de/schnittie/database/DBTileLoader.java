package de.schnittie.database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class DBTileLoader {
    public static void loadTiles() throws SQLException, URISyntaxException, IOException {
        DBinteractions db = DBinteractions.getInstance();
        db.putTilesIntoDB("/images");
        // wouldn't do that... afterwards the app can't talk to the DB anymore
        db.close();
    }
}
