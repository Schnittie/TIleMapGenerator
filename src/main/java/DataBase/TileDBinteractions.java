package DataBase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TileDBinteractions {
    private Connection conn;

    public TileDBinteractions() throws SQLException {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LegoBattlesMapGenerator"
                    , DBhelper.readLine("C:\\Users\\laure\\Desktop\\user.txt"), DBhelper.readLine("C:\\Users\\laure\\Desktop\\password.txt"));
            conn.setSchema("legoBattlesMapGeneratorSchema");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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