package model;

import java.time.LocalDateTime;

public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String courseId;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    
    public enum EnrollmentStatus {
        ENROLLED, DROPPED
    }
    
    public Enrollment(String enrollmentId, String studentId, String courseId) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = LocalDateTime.now();
        this.status = EnrollmentStatus.ENROLLED;
    }
    
    // Getters and setters
    public String getEnrollmentId() {
        return enrollmentId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public EnrollmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
    
    public void dropCourse() {
        this.status = EnrollmentStatus.DROPPED;
    }
    
    @Override
    public String toString() {
        return enrollmentId + "," + studentId + "," + courseId + "," + 
               enrollmentDate + "," + status;
    }
    
    public static Enrollment fromString(String str) {
        String[] parts = str.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid enrollment string format");
        }
        
        Enrollment enrollment = new Enrollment(parts[0], parts[1], parts[2]);
        enrollment.enrollmentDate = LocalDateTime.parse(parts[3]);
        enrollment.status = EnrollmentStatus.valueOf(parts[4]);
        return enrollment;
    }
}