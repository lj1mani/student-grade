public class Grade {

    private int id;
    private int studentId;
    private int subjectId;
    private int grade;

    public Grade(int id, int studentId, int subjectId, int grade) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getGrade() {
        return grade;
    }
}