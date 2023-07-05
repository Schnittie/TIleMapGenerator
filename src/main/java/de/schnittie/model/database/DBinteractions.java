package de.schnittie.model.database;

import de.schnittie.model.businesscode.board.PairOfCoordinates;
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


    private static final String DefaultDB_FOLDER = InstallationHandler.getResourcesURLandIfNotExistsCreate() + File.separator;


    public String getTileFolder() {
        return (dbFolder + File.separator + InstallationHandler.getTileFolderName());
    }

    public String getDbFolder() {
        return dbFolder;
    }

    private Connection conn;
    private String dbFolder = DBinteractions.DefaultDB_FOLDER;

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
        config.enforceForeignKeys(true);
        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        dataSource.setUrl("jdbc:sqlite:" + dbFolder + "TileMapGeneratorDB.db");

        this.conn = dataSource.getConnection();
        conn.setAutoCommit(true);
    }

    public DBinteractions setDbFolder(String dbFolder) throws SQLException {
        this.dbFolder = dbFolder;
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        dataSource.setUrl("jdbc:sqlite:" + dbFolder + File.separator + "TileMapGeneratorDB.db");

        this.conn = dataSource.getConnection();
        conn.setAutoCommit(true);
        return this;
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

    public HashMap<Integer, Integer> getProbabilityMap() {
        //Mappes Tiles to their probability

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, Integer> resultMap = new HashMap<>();
            statement = conn.prepareStatement(

                    "SELECT id, probability FROM tile");

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

    public HashMap<Integer, Integer> getReverseDirection() {
        //Mapps Directions with their reverse

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

    public HashMap<Integer, PairOfCoordinates> getDirectionChanges() {
        //Mapps a direction to what the direction means in terms of Coordinates

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, PairOfCoordinates> resultMap = new HashMap<>();
            statement = conn.prepareStatement(

                    "SELECT id, x_change, y_change FROM direction"
            );
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int x_change = resultSet.getInt("x_change");
                int y_change = resultSet.getInt("y_change");
                resultMap.put(id, new PairOfCoordinates(x_change, y_change));
            }
            return resultMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public ArrayList<Integer> getPossibleTileIDs() {
        //Returns an Array of all possible Tiles

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            ArrayList<Integer> resultList = new ArrayList<>();
            statement = conn.prepareStatement(
                    "SELECT id FROM tile");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultList.add(resultSet.getInt("id"));
            }
            return resultList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public HashMap<Integer, String> getDirectionNameMap() {
        //Mappes direction to their name

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, String> resultMap = new HashMap<>();
            statement = conn.prepareStatement(

                    "SELECT id, name FROM direction");

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                resultMap.put(id, name);
            }
            return resultMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public void putTileIntoDB(String imageUrl, int probability) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(

                    "INSERT INTO tile (filepath, probability) VALUES (?,?) ON CONFLICT (filepath) DO NOTHING");
            statement.setString(1, imageUrl);
            statement.setInt(2, probability);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
        }
    }

    public void putRuleIntoDB(RuleTO rule) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(
                    "INSERT INTO rule (this_tile, that_tile, next_to) VALUES (?,?,?) ON CONFLICT DO NOTHING");
            statement.setInt(1, rule.this_tile());
            statement.setInt(2, rule.that_tile());
            statement.setInt(3, rule.next_to());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
        }
    }

    public void putListOfRulesIntoDB(List<RuleTO> rules) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(
                    "INSERT INTO rule (this_tile, that_tile, next_to) VALUES (?,?,?) ON CONFLICT DO NOTHING");

            final int batchSize = 100; // Set an appropriate batch size

            int count = 0;
            for (RuleTO rule : rules) {
                statement.setInt(1, rule.this_tile());
                statement.setInt(2, rule.that_tile());
                statement.setInt(3, rule.next_to());
                statement.addBatch(); // Add the statement to the batch
                if (++count % batchSize == 0) {
                    statement.executeBatch(); // Execute the batch of statements
                }
            }

            statement.executeBatch(); // Execute any remaining statements in the batch
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

    public HashMap<Integer, String> getFilePathMap() {
        //Mapps TileIDs to their file paths

        HashMap<Integer, String> returnMap = new HashMap<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("SELECT id,filepath FROM tile");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                returnMap.put(resultSet.getInt(1),
                        (getTileFolder() + File.separator + (resultSet.getString("filepath"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
        return returnMap;
    }

    public ArrayList<RuleTO> getAllRules() {
        //Returns an Array of all Rules
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            ArrayList<RuleTO> resultList = new ArrayList<>();
            statement = conn.prepareStatement(
                    "SELECT * FROM rule ORDER BY that_tile, next_to");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultList.add(new RuleTO(
                        resultSet.getInt("id"),
                        resultSet.getInt("this_tile"),
                        resultSet.getInt("that_tile"),
                        resultSet.getInt("next_to")));
            }
            return resultList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public RuleTO getRuleById(int ruleID) {
        //Returns one Rule
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement(
                    "SELECT * FROM rule WHERE id = ?");
            statement.setInt(1, ruleID);
            resultSet = statement.executeQuery();
            return new RuleTO(
                    resultSet.getInt("id"),
                    resultSet.getInt("this_tile"),
                    resultSet.getInt("that_tile"),
                    resultSet.getInt("next_to"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }


    public void removeRule(int ruleToRemove) {
        //Returns one Rule
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement(
                    "DELETE * FROM rule WHERE id = ?");
            statement.setInt(1, ruleToRemove);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(resultSet);
        }
    }

    public void setProbabilityForTile(int tileID, int probability) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(
                    "UPDATE tile SET probability = ? WHERE id = ?");
            statement.setInt(1, probability);
            statement.setInt(2, tileID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
        }
    }
}
