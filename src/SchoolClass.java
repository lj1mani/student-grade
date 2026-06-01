public class SchoolClass {

    private int id;
    private String className;

    public SchoolClass(int id, String className) {
        this.id = id;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /*
    @Override
    public String toString() {
        return className;
    }
    */
}