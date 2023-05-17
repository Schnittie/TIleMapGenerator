package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DBhelper {
    public static String readLine(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        reader.close();
        return line;
    }
}
