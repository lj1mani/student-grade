public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private SchoolClass schoolClass;

    public Student(int id, String firstName, String lastName, SchoolClass schoolClass) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.schoolClass = schoolClass;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}