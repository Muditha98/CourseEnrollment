package model;

public class Course {
    private String courseId;
    private String courseCode;
    private String title;
    private String description;
    private int credits;
    private int capacity;
    private int currentEnrollment;
    
    public Course(String courseId, String courseCode, String title, String description, 
                int credits, int capacity) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.credits = credits;
        this.capacity = capacity;
        this.currentEnrollment = 0;
    }
    
    // Getters and setters
    public String getCourseId() {
        return courseId;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getCurrentEnrollment() {
        return currentEnrollment;
    }
    
    public boolean hasAvailableSeats() {
        return currentEnrollment < capacity;
    }
    
    public boolean enrollStudent() {
        if (hasAvailableSeats()) {
            currentEnrollment++;
            return true;
        }
        return false;
    }
    
    public void dropStudent() {
        if (currentEnrollment > 0) {
            currentEnrollment--;
        }
    }
    
    @Override
    public String toString() {
        return courseId + "," + courseCode + "," + title + "," + description + "," + 
               credits + "," + capacity + "," + currentEnrollment;
    }
    
    public static Course fromString(String str) {
        String[] parts = str.split(",");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid course string format");
        }
        
        Course course = new Course(
            parts[0],
            parts[1],
            parts[2],
            parts[3],
            Integer.parseInt(parts[4]),
            Integer.parseInt(parts[5])
        );
        course.currentEnrollment = Integer.parseInt(parts[6]);
        return course;
    }
}