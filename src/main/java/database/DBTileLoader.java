package database;

import java.sql.SQLException;

public class DBTileLoader {
    public static void loadTiles() throws SQLException {
        DBinteractions db = DBinteractions.getInstance();
        db.putTilesIntoDB("src/main/images");
        db.close();
    }
}
