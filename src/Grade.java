public class Grade {

    private int id;
    private String subjectName;
    private int gradeValue;

    public Grade(int id, String subjectName, int gradeValue) {
        this.id = id;
        this.subjectName = subjectName;
        this.gradeValue = gradeValue;
    }

    public int getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getGradeValue() {
        return gradeValue;
    }

    @Override
    public String toString() {
        return subjectName + " : " + gradeValue;
    }
}