import model.Student;
import view.EnrollmentView;

public class Main {
    public static void main(String[] args) {
        // Create a sample student
        Student student = new Student("S001", "John Doe");
        
        // Launch the enrollment view
        new EnrollmentView(student);
    }
}