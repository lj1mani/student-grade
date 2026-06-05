public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private int classId;

    public Student(int id, String firstName, String lastName, int classId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classId = classId;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getClassId() { return classId; }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}