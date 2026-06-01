import java.time.LocalDate;

public class Grade {

    private int id;
    private Student student;
    private Subject subject;
    private int grade;
    private LocalDate date;

    public Grade(int id, Student student, Subject subject,
                 int grade, LocalDate date) {

        this.id = id;
        this.student = student;
        this.subject = subject;
        this.grade = grade;
        this.date = date;
    }

    public int getGrade() {
        return grade;
    }

    public Student getStudent() {
        return student;
    }

    public Subject getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }
}