public class GradeView {

    private String subjectName;
    private int grade;

    public GradeView(String subjectName, int grade) {
        this.subjectName = subjectName;
        this.grade = grade;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return subjectName + " → " + grade;
    }
}