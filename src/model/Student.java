package model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentId;
    private String name;
    private List<String> enrolledCourseIds;
    
    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.enrolledCourseIds = new ArrayList<>();
    }
    
    // Getters and setters
    public String getStudentId() {
        return studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }
    
    public void enrollInCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }
    
    public void dropCourse(String courseId) {
        enrolledCourseIds.remove(courseId);
    }
    
    public boolean isEnrolledIn(String courseId) {
        return enrolledCourseIds.contains(courseId);
    }
}