import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchoolDAO {

    public boolean addClass(String className) {

        // SQL query with placeholder (?) to prevent SQL injection
        String sql = "INSERT INTO classes (class_name) VALUES (?)";

        // try-with-resources automatically closes connection and statement
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the class name into the SQL query
            pstmt.setString(1, className);

            // execute insert and get number of affected rows
            int rowsInserted = pstmt.executeUpdate();

            // if at least 1 row is inserted → success
            return rowsInserted > 0;

        } catch (SQLException e) {

            // print error for debugging
            System.out.println("Error while adding class: " + className);
            e.printStackTrace();

            return false;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Returns all classes from database

    public List<SchoolClass> getAllClasses() {

        List<SchoolClass> classes = new ArrayList<>();

        String sql = "SELECT class_id, class_name FROM classes ORDER BY class_name";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // loop through all database rows
            while (rs.next()) {

                SchoolClass sc = new SchoolClass(
                        rs.getInt("class_id"),
                        rs.getString("class_name")
                );

                classes.add(sc);
            }

        } catch (SQLException e) {
            System.out.println("Error while loading classes");
            e.printStackTrace();
        }

        return classes;
    }
}
