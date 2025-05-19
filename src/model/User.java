package model;

public class User {
    private String username;
    private String password;
    private String role;
    private Student student;

    public User(String username, String password, String role, Student student) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.student = student;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public String toString() {
        return username + "," + password + "," + role + "," + student.getStudentId() + "," + student.getName();
    }

    public static User fromString(String str) {
        String[] parts = str.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid user string format");
        }
        Student student = new Student(parts[3], parts[4]);
        return new User(parts[0], parts[1], parts[2], student);
    }
}
