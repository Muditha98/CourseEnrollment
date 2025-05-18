package controller;

import model.Course;
import model.Enrollment;
import model.Student;
import util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnrollmentController {
    private static final String ENROLLMENTS_FILE = "data/enrollments.txt";
    private static final String COURSES_FILE = "data/courses.txt";
    
    private List<Course> courses;
    private List<Enrollment> enrollments;
    
    public EnrollmentController() {
        loadData();
    }
    
    private void loadData() {
        try {
            // Load courses
            List<String> courseLines = FileHandler.readFile(COURSES_FILE);
            courses = new ArrayList<>();
            for (String line : courseLines) {
                courses.add(Course.fromString(line));
            }
            
            // Load enrollments
            List<String> enrollmentLines = FileHandler.readFile(ENROLLMENTS_FILE);
            enrollments = new ArrayList<>();
            for (String line : enrollmentLines) {
                enrollments.add(Enrollment.fromString(line));
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            courses = new ArrayList<>();
            enrollments = new ArrayList<>();
        }
    }
    
    private void saveEnrollments() {
        try {
            List<String> lines = enrollments.stream()
                    .map(Enrollment::toString)
                    .collect(Collectors.toList());
            FileHandler.writeFile(ENROLLMENTS_FILE, lines);
        } catch (IOException e) {
            System.err.println("Error saving enrollments: " + e.getMessage());
        }
    }
    
    private void saveCourses() {
        try {
            List<String> lines = courses.stream()
                    .map(Course::toString)
                    .collect(Collectors.toList());
            FileHandler.writeFile(COURSES_FILE, lines);
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }
    
    // For student enrollment
    public boolean enrollStudent(Student student, Course course) {
        // Check if student is already enrolled
        if (student.isEnrolledIn(course.getCourseId())) {
            return false;
        }
        
        // Check if course has available seats
        if (!course.hasAvailableSeats()) {
            return false;
        }
        
        // Create enrollment
        String enrollmentId = UUID.randomUUID().toString();
        Enrollment enrollment = new Enrollment(enrollmentId, student.getStudentId(), course.getCourseId());
        enrollments.add(enrollment);
        
        // Update student
        student.enrollInCourse(course.getCourseId());
        
        // Update course
        course.enrollStudent();
        
        // Save changes
        saveEnrollments();
        saveCourses();
        
        return true;
    }
    
    // For admin enrollment (polymorphic - can enroll even if course is full)
    public boolean enrollStudentAsAdmin(Student student, Course course) {
        // Check if student is already enrolled
        if (student.isEnrolledIn(course.getCourseId())) {
            return false;
        }
        
        // Create enrollment
        String enrollmentId = UUID.randomUUID().toString();
        Enrollment enrollment = new Enrollment(enrollmentId, student.getStudentId(), course.getCourseId());
        enrollments.add(enrollment);
        
        // Update student
        student.enrollInCourse(course.getCourseId());
        
        // Update course (for admin, we bypass capacity check)
        course.enrollStudent();
        
        // Save changes
        saveEnrollments();
        saveCourses();
        
        return true;
    }
    
    public boolean dropCourse(Student student, Course course) {
        // Check if student is enrolled
        if (!student.isEnrolledIn(course.getCourseId())) {
            return false;
        }
        
        // Find the enrollment
        List<Enrollment> studentEnrollments = getEnrollmentsForStudent(student.getStudentId());
        Enrollment enrollmentToUpdate = null;
        
        for (Enrollment enrollment : studentEnrollments) {
            if (enrollment.getCourseId().equals(course.getCourseId()) && 
                enrollment.getStatus() == Enrollment.EnrollmentStatus.ENROLLED) {
                enrollmentToUpdate = enrollment;
                break;
            }
        }
        
        if (enrollmentToUpdate == null) {
            return false;
        }
        
        // Update enrollment status
        enrollmentToUpdate.dropCourse();
        
        // Update student
        student.dropCourse(course.getCourseId());
        
        // Update course
        course.dropStudent();
        
        // Save changes
        saveEnrollments();
        saveCourses();
        
        return true;
    }
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
    
    public List<Course> getAvailableCourses() {
        return courses.stream()
                .filter(Course::hasAvailableSeats)
                .collect(Collectors.toList());
    }
    
    public List<Course> getEnrolledCourses(Student student) {
        List<String> enrolledCourseIds = student.getEnrolledCourseIds();
        return courses.stream()
                .filter(course -> enrolledCourseIds.contains(course.getCourseId()))
                .collect(Collectors.toList());
    }
    
    public List<Enrollment> getEnrollmentsForStudent(String studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }
    
    // For demonstration purposes, add sample courses if none exist
    public void addSampleCoursesIfNeeded() {
        if (courses.isEmpty()) {
            courses.add(new Course("C001", "CS101", "Introduction to Programming", "Basic programming concepts", 3, 30));
            courses.add(new Course("C002", "CS201", "Data Structures", "Advanced data structures", 4, 25));
            courses.add(new Course("C003", "CS301", "Database Systems", "Database design and SQL", 3, 20));
            courses.add(new Course("C004", "MATH101", "Calculus I", "Limits, derivatives, and integrals", 4, 35));
            courses.add(new Course("C005", "ENG101", "English Composition", "Academic writing skills", 3, 30));
            saveCourses();
        }
    }
}