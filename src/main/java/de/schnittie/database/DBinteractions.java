package de.schnittie.database;

import de.schnittie.businesscode.MapGenerationException;
import de.schnittie.businesscode.Pair;
import de.schnittie.frontend.FrontendGUI;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.sqlite.core.DB;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class DBinteractions {

    private static String DB_FOLDER =
            FrontendGUI.FILES_FOLDER + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
    private Connection conn;

    private static final DBinteractions dBInteractions;

    static {
        try {
            dBInteractions = new DBinteractions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBinteractions getInstance() {
        return dBInteractions;
    }


    private DBinteractions() throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true); // Optional: Enable foreign key constraints
        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        /**
         * Regarding this: I'd suggest having an empty, prebuilt but empty DB file in the resources folder.
         * At app-Start, you check for the existence of a copy of the DB file in an appropriate destination folder.
         * Best case would be to choose a suitable folder using https://github.com/harawata/appdirs
         * If the file doesn't exist yet, copy it from the resources using the Classloader.
         * You then can fill the base data in there (in case the data depends on the target machines environment),
         * or just prefill any kind of universal data.
         */
        dataSource.setUrl("jdbc:sqlite:"+ DB_FOLDER +"LegoBattlesMapGeneratorDB.db");

        // Get a connection from the data source
        this.conn = dataSource.getConnection();
        // I'd set this to true tbh... for your usecase, there is no reason to not commit changes immediately.
        conn.setAutoCommit(true); // Optional: Set auto-commit behavior
    }

    public void close() throws SQLException {
        conn.close();
    }

    public void putTilesIntoDB(String folderFilepath) throws SQLException, URISyntaxException, IOException {

        URI uri = getClass().getResource(folderFilepath).toURI();
        Path myPath = Paths.get(uri);
        Iterator<Path> iterator = Files.walk(myPath, 1).iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            File file = path.toFile();
            if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                System.out.println("Adding tile to DB: "+ path.getFileName());
                String imageUrl = file.getAbsolutePath();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO tile_content (filepath) VALUES (?) ON CONFLICT (filepath) DO NOTHING");
                stmt.setString(1, imageUrl);
                stmt.executeUpdate();
            }
        }
    }

    public List<Integer> putRuleIntoDB(int this_tile, int next_to, int that_tile, boolean can_it_be) throws SQLException {
        List<Integer> createdRuleIds = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO rule (this_tile, next_to, that_tile , can_it_be) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, this_tile);
        stmt.setInt(2, next_to);
        stmt.setInt(3, that_tile);
        stmt.setInt(4, can_it_be ? 1 : 0);
        stmt.executeUpdate();

        // Get the generated rule ID(s)
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        while (generatedKeys.next()) {
            int ruleId = generatedKeys.getInt(1);
            createdRuleIds.add(ruleId);
        }

        // Get the reverse_id of next_to from the direction table
        PreparedStatement directionStmt = conn.prepareStatement(
                "SELECT reverse_id FROM direction WHERE id = ?");
        directionStmt.setInt(1, next_to);
        ResultSet directionResult = directionStmt.executeQuery();
        if (directionResult.next()) {
            int reverseNextTo = directionResult.getInt("reverse_id");

            // Insert the reversed rule into the rule table
            PreparedStatement reversedStmt = conn.prepareStatement(
                    "INSERT INTO rule (this_tile, next_to, that_tile, can_it_be) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            reversedStmt.setInt(1, that_tile);
            reversedStmt.setInt(2, reverseNextTo);
            reversedStmt.setInt(3, this_tile);
            stmt.setInt(4, can_it_be ? 1 : 0);
            reversedStmt.executeUpdate();

            // Get the generated rule ID(s)
            generatedKeys = reversedStmt.getGeneratedKeys();
            while (generatedKeys.next()) {
                int reversedRuleId = generatedKeys.getInt(1);
                createdRuleIds.add(reversedRuleId);
            }
        }

        return createdRuleIds;
    }


    public RuleCreationTO getNewRule() throws SQLException, NoMoreRulesException {
        // Query the database for tile pairs without rules
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT this_tile.id AS this_tile_id, that_tile.id AS that_tile_id, direction.id AS direction_id, this_tile.filepath AS this_tile_filepath, that_tile.filepath AS that_tile_filepath " +
                        "FROM tile_content AS this_tile, tile_content AS that_tile, direction " +
                        "WHERE NOT EXISTS (SELECT * FROM rule WHERE this_tile = this_tile.id AND next_to = direction.id AND that_tile = that_tile.id) " +
                        "AND NOT EXISTS (SELECT * FROM rule WHERE this_tile = that_tile.id AND next_to = direction.reverse_id AND that_tile = this_tile.id) " +
                        "ORDER BY this_tile_id ASC, that_tile_id ASC, direction_id ASC " +
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
            throw new NoMoreRulesException();
        }
    }

    public void revertRule(List<Integer> lastRule) {
        for (Integer ruleId : lastRule) {
            try {
                PreparedStatement statement = conn.prepareStatement(
                        "DELETE FROM rule WHERE id = ?"
                );
                statement.setInt(1, ruleId);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getNumberOfTiles() {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM tile_content");
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> canThisBeHere(List<Integer> tileInQuestion, int whereIamRelativeToCaller, List<Integer> listOfPossibilities) throws MapGenerationException {
        //list of Possibilities Now is the List of the possibilities the Tile from where the propagation is coming from can be
        //tile in question is the tile this tile could be
        if (listOfPossibilities.isEmpty()) {
            throw new MapGenerationException();
        }
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement(
                    "SELECT this_tile FROM rule " +
                            "WHERE this_tile IN (" + getParameterPlaceholders(tileInQuestion.size()) + ") " +
                            "AND next_to = ? " +
                            "AND can_it_be = 1 " +
                            "AND that_tile IN (" + getParameterPlaceholders(listOfPossibilities.size()) + ")");

            int i = 1;
            for (Integer tile : tileInQuestion) {
                statement.setInt(i++, tile);
            }
            statement.setInt(i++, whereIamRelativeToCaller);
            for (Integer possibility : listOfPossibilities) {
                statement.setInt(i++, possibility);
            }
            resultSet = statement.executeQuery();
            List<Integer> returnList = new ArrayList<>();
            while (resultSet.next())
            {
                returnList.add(resultSet.getInt("this_tile"));
            }
            return returnList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    private static void close(AutoCloseable closable) {
        try {
            if (closable != null) {
                closable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // do nothing
        }
    }

    public HashMap<Integer, Integer> getProbabilityMap(List<Integer> possibleStates) {
        try {
            HashMap<Integer, Integer> resultMap = new HashMap<>();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, probability FROM tile_content WHERE id IN (" + getParameterPlaceholders(possibleStates.size()) + ")");

            for (int i = 0; i < possibleStates.size(); i++) {
                statement.setInt(i + 1, possibleStates.get(i));
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int probability = resultSet.getInt("probability");
                resultMap.put(id, probability);
            }
            return resultMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getParameterPlaceholders(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("?");
            if (i < count - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public HashMap<Integer, Pair> getDirectionChanges() {
        try {
            HashMap<Integer, Pair> resultMap = new HashMap<>();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, x_change, y_change FROM direction");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int x_change = resultSet.getInt("x_change");
                int y_change = resultSet.getInt("y_change");
                resultMap.put(id, new Pair(x_change, y_change));
            }
            return resultMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getPossibleTileIDs(int possibleTileSize) {
        try {
            int[] resultArray = new int[possibleTileSize];
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id FROM tile_content");
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                resultArray[i++] = resultSet.getInt("id");
            }
            return resultArray;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFilePath(int content) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT filepath FROM tile_content WHERE id = ?");
            statement.setInt(1, content);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("filepath");
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
