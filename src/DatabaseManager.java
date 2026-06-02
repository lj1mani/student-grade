import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DatabaseManager {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    // Load database config from db.properties
    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            props.load(fis);
            URL = props.getProperty("DB_URL");
            USER = props.getProperty("DB_USER");
            PASSWORD = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load database configuration. Please check db.properties Or Missing db.properties file! Please copy db.properties.example and configure it.");
        }
    }


    // Returns a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void ensureClassesTable() {

        String sql =
                "CREATE TABLE IF NOT EXISTS classes ("
                        + "class_id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "class_name VARCHAR(10) NOT NULL UNIQUE"
                        + ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

            System.out.println("Classes table ready.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ////////////// INITALIZATION ///////////////////////////////////////////////////////////////
    public static void initDatabase() {
        ensureClassesTable();
        // later:
        // ensureStudentsTable();
        // ensureSubjectsTable();
        // ensureGradesTable();
    }

    }






