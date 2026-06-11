public class Subject {

    private int id;
    private String subjectName;
    private int classId;

    public Subject(int id, String subjectName, int classId) {
        this.id = id;
        this.subjectName = subjectName;
        this.classId = classId;
    }

    public int getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getClassId() {
        return classId;
    }

    @Override
    public String toString() {
        return subjectName;
    }
}