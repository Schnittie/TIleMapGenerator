package DataBase;

import java.sql.SQLException;

public class DBService {
    public static void main(String[] args) {
        try {
            TileDBinteractions tileBank = new TileDBinteractions();
            tileBank.putTilesIntoDB("src/main/images");
            tileBank.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
