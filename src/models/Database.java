package models;

import models.datastructures.DataScore;
import models.datastructures.DataWords;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.sqlite.JDBC.createConnection;

/**
 * See klass tegeleb andmebaasi ühenduse ja "igasuguste" päringutega tabelitest.
 * Alguses on ainult ühenduse jaoks funktsionaalsus
 */
public class Database {
    /**
     * Algselt ühendust pole
     */
    private Connection connection;
    /**
     * Andmebaasi ühenduse string
     */
    private String databaseUrl;
    /**
     * Loodud mudel
     */
    private Model model;

    /**
     * Klassi andmebaas konstruktor
     * @param model loodud mudel
     */
    public Database(Model model) {
        this.model = model;
        this.databaseUrl = "jdbc:sqlite:" + model.getDatabaseFile();
        this.connection = createConnection();
        this.selectUniqueCategories();
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection((databaseUrl));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database connection");
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failsed to close database connection");
        }
    }

    /**
     * Loob andmebaasiga ühenduse
     * @return andmebaasi ühenduse
     */
    private Connection dbConnection() throws SQLException {
        // https://stackoverflow.com/questions/13891006/
        if(connection != null) {
            connection.close();
        }
        connection = DriverManager.getConnection(databaseUrl);
        return connection;
    }

    private void selectUniqueCategories() {
        String sql = "SELECT DISTINCT(category) as category FROM words ORDER BY category;";
        List<String> categories = new ArrayList<>();  // Tühi kategooriate list
        try {
            Connection connection = this.dbConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String category = rs.getString("category");
                categories.add(category);  // Lisa kategooria listi kategooriad (categories)
            }
            categories.add(0, model.getChooseCategory());  // Esimeseks "Kõik kategooriad"
            // System.out.println(categories);
            String[] result = categories.toArray(new String[0]);  // List<String> tehakse stringi massiiviks String[]
            model.setCmbCategories(result);  // Seddista kategooriad mudelisse.

            connection.close();  // db ühendus sulgeda
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectScores() {
        String sql = "SELECT * FROM scores ORDER BY gametime, playertime DESC, playername;";
        List<DataScore> data = new ArrayList<>();
        try {
            Connection connection = this.dbConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            model.getDataScores().clear();  // Tühjenda mudelist listi sisu

            while (rs.next()) {
                String datetime = rs.getString("playertime");
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  // mänguaeg tehakse ümber andmebaasile sobivaks formaadiks
                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongChar = rs.getString("wrongcharacters");
                int timeSeconds = rs.getInt("gametime");
                // System.out.println(datetime + "|" + playerTime);  // Test
                // Lisa listi kirje
                data.add(new DataScore(playerTime, playerName, guessWord, wrongChar, timeSeconds));
            }
            model.setDataScores(data);  // Muuda andmed mudelis

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Andmebaasist juhuslik sõna
     * @param category juhusliku sõna valimine andmebaasist
     * @return
     */
    public DataWords selectRandomWord(String category) {
        String sql = "SELECT * FROM words ";
        if (!category.equals(model.getChooseCategory())) {
            sql += "WHERE category = ? ";
        }
        sql += "ORDER BY RANDOM() LIMIT 1;";
        try {
            Connection connection = this.dbConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            if (!category.equals(model.getChooseCategory())) {
                pstmt.setString(1, category);
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String word = rs.getString("word").toUpperCase();
                String wordCategory = rs.getString("category");
                return new DataWords(id, word, wordCategory);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void saveScore(String playerName, String guessWord, String wrongCharacters, int gameTime) {
        String sql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters, gametime) VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = this.dbConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setString(2, playerName);
            pstmt.setString(3, guessWord);
            pstmt.setString(4, wrongCharacters);
            pstmt.setInt(5, gameTime);
            pstmt.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving score to the database", ex);
        }
    }

}
