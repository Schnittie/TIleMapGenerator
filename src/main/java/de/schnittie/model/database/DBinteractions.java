package de.schnittie.model.database;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.Pair;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBinteractions {


    private static final String DB_FOLDER = InstallationHandler.getResourcesURLandIfNotExistsCreate()
            + File.separator;

    private static final String TILEFOLDER = DB_FOLDER + File.separator + "TileImages" + File.separator;

    public static String getTILEFOLDER() {
        return TILEFOLDER;
    }

    public static String getDbFolder() {
        return DB_FOLDER;
    }

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
        dataSource.setUrl("jdbc:sqlite:" + DB_FOLDER + "LegoBattlesMapGeneratorDB.db");

        // Get a connection from the data source
        this.conn = dataSource.getConnection();
        // I'd set this to true tbh... for your usecase, there is no reason to not commit changes immediately.
        conn.setAutoCommit(true); // Optional: Set auto-commit behavior
    }

    public void close() throws SQLException {
        conn.close();
    }

    public int getNumberOfTiles() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("SELECT COUNT(*) FROM tile");
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
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
                    "SELECT DISTINCT this_tile FROM rule " +
                            "WHERE this_tile IN (" + getParameterPlaceholders(tileInQuestion.size()) + ") " +
                            "AND next_to = ? " +
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
            while (resultSet.next()) {
                returnList.add(resultSet.getInt("this_tile"));
            }
            if (returnList.isEmpty()){
                throw new MapGenerationException();
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
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, Integer> resultMap = new HashMap<>();
            statement = conn.prepareStatement(
                    "SELECT id, probability FROM tile WHERE id IN (" + getParameterPlaceholders(possibleStates.size()) + ")");

            for (int i = 0; i < possibleStates.size(); i++) {
                statement.setInt(i + 1, possibleStates.get(i));
            }

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int probability = resultSet.getInt("probability");
                resultMap.put(id, probability);
            }
            return resultMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
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
    public HashMap<Integer, Integer> getReverseDirection(){
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, Integer> resultMap = new HashMap<>();
            statement = conn.prepareStatement(
                    "SELECT id, reverse_id FROM direction");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int reverse_id = resultSet.getInt("reverse_id");
                resultMap.put(id, reverse_id);
            }
            return resultMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public HashMap<Integer, Pair> getDirectionChanges() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, Pair> resultMap = new HashMap<>();
            statement = conn.prepareStatement(
                    "SELECT id, x_change, y_change FROM direction");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int x_change = resultSet.getInt("x_change");
                int y_change = resultSet.getInt("y_change");
                resultMap.put(id, new Pair(x_change, y_change));
            }
            return resultMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public int[] getPossibleTileIDs(int possibleTileSize) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            int[] resultArray = new int[possibleTileSize];
            statement = conn.prepareStatement(
                    "SELECT id FROM tile");
            resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                resultArray[i++] = resultSet.getInt("id");
            }
            return resultArray;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public String getFilePath(int content) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("SELECT filepath FROM tile WHERE id = ?");
            statement.setInt(1, content);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return TILEFOLDER + File.separator + (resultSet.getString("filepath"));
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public void putTileIntoDB(String imageUrl) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(
                    "INSERT INTO tile (filepath) VALUES (?) ON CONFLICT (filepath) DO NOTHING");
            statement.setString(1, imageUrl);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
        }
    }
    public void putRuleIntoDB(int this_tile, int that_tile, int direction) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(
                    "INSERT INTO rule (this_tile, that_tile, next_to) VALUES (?,?,?) ON CONFLICT DO NOTHING");
            statement.setInt(1, this_tile);
            statement.setInt(2, that_tile);
            statement.setInt(3, direction);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
        }
    }

    public void putDirectionInDB(int id, String directionName, int x_change, int y_change, int reverse_id) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(
                    "INSERT INTO direction (id, name, x_change, y_change, reverse_id) VALUES (?,?,?,?,?)");
            statement.setInt(1, id);
            statement.setString(2, directionName);
            statement.setInt(3, x_change);
            statement.setInt(4, y_change);
            statement.setInt(5, reverse_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
        }
    }
}
