package it.saimao.burma_dictionary;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalDatabase {
    private static final String DB_DIR = System.getenv("ProgramData") + "\\BurmaDictionary";
    private static final String DB_PATH = DB_DIR + "\\mm_dictionary.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static void ensureDatabaseExists() throws IOException {
        Path dbDirPath = Path.of(DB_DIR);
        if (Files.notExists(dbDirPath)) {
            Files.createDirectories(dbDirPath);
        }
        Path dbPath = Path.of(DB_PATH);
        if (Files.notExists(dbPath)) {
            try (InputStream in = LocalDatabase.class.getResourceAsStream("/mm_dictionary.db")) {
                if (in == null) {
                    throw new IOException("Resource database not found: /mm_dictionary.db");
                }
                Files.copy(in, dbPath);
            }
        }
    }

    public static void testConnection() {
        try {
            Connection con = DriverManager.getConnection(URL);
            System.out.println("Connected to SQLite successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Data> getAllData() {
        var list = new ArrayList<Data>();
        String query = "SELECT * FROM dictionary ORDER BY RANDOM();";
        try (Connection con = getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("_id");
                String word = rs.getString("word");
                String stripWord = rs.getString("stripword");
                String title = rs.getString("title");
                String definition = rs.getString("definition");
                String keywords = rs.getString("keywords");
                String synonym = rs.getString("synonym");
                String picture = rs.getString("picture");

                Data data = new Data(id, word, stripWord, title, definition, keywords, synonym, picture);
                list.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<Data> getDataWithWord(String word) {
        var list = new ArrayList<Data>();
        String query = "SELECT id, word, state, def FROM mydblist WHERE word LIKE ? ORDER BY stripword";
        try (Connection con = getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, word + "%");
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("_id");
                String stripWord = rs.getString("stripword");
                String title = rs.getString("title");
                String definition = rs.getString("definition");
                String keywords = rs.getString("keywords");
                String synonym = rs.getString("synonym");
                String picture = rs.getString("picture");

                Data data = new Data(id, word, stripWord, title, definition, keywords, synonym, picture);
                list.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
