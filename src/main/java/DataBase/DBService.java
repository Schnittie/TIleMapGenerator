package DataBase;

import java.sql.SQLException;

public class DBService {
    public static void main(String[] args) throws SQLException {
        DBinteractions db = DBinteractions.getInstance();
        db.putTilesIntoDB("src/main/images");
        db.close();
    }
}
