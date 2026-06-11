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

    public static void ensureSubjectsTable() {

        String sql =
                "CREATE TABLE IF NOT EXISTS subjects (" +
                        "subject_id INT AUTO_INCREMENT PRIMARY KEY," +
                        "subject_name VARCHAR(50) NOT NULL," +
                        "class_id INT NOT NULL," +
                        "FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE CASCADE" +
                        ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

            System.out.println("Subjects table ready.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ensureGradesTable() {

        String sql =
                "CREATE TABLE IF NOT EXISTS grades (" +
                        "grade_id INT AUTO_INCREMENT PRIMARY KEY," +
                        "student_id INT NOT NULL," +
                        "subject_id INT NOT NULL," +
                        "grade_value INT NOT NULL," +
                        "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE," +
                        "FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE" +
                        ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

            System.out.println("Grades table ready.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    ////////////// INITALIZATION ///////////////////////////////////////////////////////////////
    public static void initDatabase() {
        ensureClassesTable();
        //ensureSubjectsTable();
        //ensureGradesTable();
    }

    }






