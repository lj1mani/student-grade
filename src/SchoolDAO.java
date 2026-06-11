import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchoolDAO {

    /* ****************************************** */
    /* ************ CLASSES ********************* */

    public boolean addClass(String className) {

        if (classExists(className)) {
            return false; // already exists
        }


        String sql = "INSERT INTO classes (class_name) VALUES (?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, className);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {

            // duplicate entry (MySQL error code 1062)
            if (e.getErrorCode() == 1062) {
                System.out.println("Class already exists: " + className);
            } else {
                e.printStackTrace();
            }

            return false;
        }
    }

    public boolean classExists(String className) {

        String sql = "SELECT 1 FROM classes WHERE class_name = ? LIMIT 1";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, className);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


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

    public boolean deleteClass(int classId) {

        String sql = "DELETE FROM classes WHERE class_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ******************************************* */
    /* ************ STUDENST ********************* */

    public boolean addStudent(String firstName, String lastName, int classId) {

        String sql = "INSERT INTO students (first_name, last_name, class_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, classId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> getStudentsByClass(int classId) {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students WHERE class_id = ? ORDER BY last_name";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("class_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public void deleteStudent(int studentId) {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ******************************************* */
    /* ************ SUBJECTS ********************* */

    public boolean addSubject(String subjectName, int classId) {

        String sql =
                "INSERT INTO subjects(subject_name, class_id) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subjectName);
            stmt.setInt(2, classId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Subject> getSubjectsByClass(int classId) {

        List<Subject> subjects = new ArrayList<>();

        String sql =
                "SELECT subject_id, subject_name, class_id " +
                        "FROM subjects " +
                        "WHERE class_id = ? " +
                        "ORDER BY subject_name";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, classId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                subjects.add(
                        new Subject(
                                rs.getInt("subject_id"),
                                rs.getString("subject_name"),
                                rs.getInt("class_id")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    public boolean deleteSubject(int subjectId) {

        String sql = "DELETE FROM subjects WHERE subject_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /* ******************************************* */
    /* ************ GRADES ********************* */

    public boolean addGrade(int studentId, int subjectId, int grade) {

        String sql =
                "INSERT INTO grades(student_id, subject_id, grade) " +
                        "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            stmt.setInt(3, grade);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<GradeView> getGradesForStudent(int studentId) {

        List<GradeView> list = new ArrayList<>();

        String sql =
                "SELECT s.subject_name, g.grade " +
                        "FROM grades g " +
                        "JOIN subjects s ON g.subject_id = s.subject_id " +
                        "WHERE g.student_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                list.add(new GradeView(
                        rs.getString("subject_name"),
                        rs.getInt("grade")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
