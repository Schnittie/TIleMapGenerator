package DataBase;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DBinteractions {
    private Connection conn;

    private static final DBinteractions dBInteractions;

    static {
        try {
            dBInteractions = new DBinteractions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBinteractions getInstance(){
        return dBInteractions;
    }


    private DBinteractions() throws SQLException {
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
                String imageUrl = file.getAbsolutePath();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO tile_content (filepath) VALUES (?)");
                stmt.setString(1, imageUrl);
                stmt.executeUpdate();
            }
        }
    }

    public void putRuleIntoDB(int this_tile, int next_to, int that_tile, boolean can_it_be) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO rule (this_tile, next_to, that_tile , allowed) VALUES (?, ?, ?, ?)");
        stmt.setInt(1, this_tile);
        stmt.setInt(2, next_to);
        stmt.setInt(3, that_tile);
        stmt.setBoolean(4, can_it_be);
        stmt.executeUpdate();

        // Get the reverse_id of next_to from the direction table
        PreparedStatement directionStmt = conn.prepareStatement(
                "SELECT reverse_id FROM direction WHERE id = ?");
        directionStmt.setInt(1, next_to);
        ResultSet directionResult = directionStmt.executeQuery();
        if (directionResult.next()) {
            int reverseNextTo = directionResult.getInt("reverse_id");

            // Insert the reversed rule into the rule table
            PreparedStatement reversedStmt = conn.prepareStatement(
                    "INSERT INTO rule (this_tile, next_to, that_tile, allowed) VALUES (?, ?, ?, ?)");
            reversedStmt.setInt(1, that_tile);
            reversedStmt.setInt(2, reverseNextTo);
            reversedStmt.setInt(3, this_tile);
            reversedStmt.setBoolean(4, can_it_be);
            reversedStmt.executeUpdate();
        }
    }

    public RuleCreationTO getNewRule() throws SQLException {
        // Query the database for tile pairs without rules
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT this_tile.id AS this_tile_id, that_tile.id AS that_tile_id, direction.id AS direction_id, this_tile.filepath AS this_tile_filepath, that_tile.filepath AS that_tile_filepath " +
                        "FROM tile_content AS this_tile, tile_content AS that_tile, direction " +
                        "WHERE NOT EXISTS (SELECT * FROM rule WHERE this_tile = this_tile.id AND next_to = direction.id AND that_tile = that_tile.id) " +
                        "AND NOT EXISTS (SELECT * FROM rule WHERE this_tile = that_tile.id AND next_to = direction.reverse_id AND that_tile = this_tile.id) " +
                        "ORDER BY random() " +
                        "LIMIT 1"
        );
        ResultSet rs = stmt.executeQuery();

        // Extract the result into a RuleCreationTO object
        if (rs.next()) {
            int thisTileId = rs.getInt("this_tile_id");
            int thatTileId = rs.getInt("that_tile_id");
            int directionId = rs.getInt("direction_id");
            String thisTileFilepath = rs.getString("this_tile_filepath");
            String thatTileFilepath = rs.getString("that_tile_filepath");
            return new RuleCreationTO(thisTileId, directionId, thatTileId, thisTileFilepath, thatTileFilepath);
        } else {
            throw new RuntimeException("No Tiles found that don't have rules");
        }
    }


}