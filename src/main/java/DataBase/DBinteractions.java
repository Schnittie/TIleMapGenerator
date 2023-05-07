package DataBase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBinteractions {
    private Connection conn;

    public DBinteractions() throws SQLException {
        conn = DriverManager.getConnection("LegoBattlesMapGenerator.legoBattlesMapGeneratorSchema.tile_content"
                , DBhelper.readLine(), "2763");
    }

    public void close() throws SQLException {
        conn.close();
    }

    public void putTilesIntoDB(String folderFilepath) throws SQLException {
        File folder = new File(folderFilepath);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                String imageUrl = file.toURI().toString();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO tile_content (filepath) VALUES (?)");
                stmt.setString(1, imageUrl);
                stmt.executeUpdate();
            }
        }
    }
}