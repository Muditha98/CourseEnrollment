<<<<<<< Updated upstream
# CourseEnrollment
=======
# Course Enrollment System

A Java Swing-based Course Enrollment System that allows students to enroll in and drop courses.

## Features

- View available courses
- Enroll in courses
- View enrolled courses
- Drop courses
- Persistent storage of course and enrollment data
- User-friendly GUI interface

## Project Structure

```
CourseEnrollment/
├── src/
│   ├── com/enrollment/
│   │   └── Main.java
│   ├── controller/
│   │   └── EnrollmentController.java
│   ├── model/
│   │   ├── Student.java
│   │   ├── Course.java
│   │   └── Enrollment.java
│   ├── view/
│   │   ├── EnrollmentView.java
│   │   └── EnrolledCoursesView.java
│   └── util/
│       └── FileHandler.java
└── data/
    ├── courses.txt
    └── enrollments.txt
```

## How to Run

1. Clone the repository
2. Compile the Java files:
   ```bash
   javac -d . src/com/enrollment/Main.java src/controller/*.java src/model/*.java src/util/*.java src/view/*.java
   ```
3. Run the application:
   ```bash
   java com.enrollment.Main
   ```

## Implementation Details

- Uses MVC (Model-View-Controller) architecture
- Implements file-based persistence using txt files
- Built with Java Swing for the GUI
- Features proper error handling and user feedback

## Requirements

- Java JDK 8 or higher
- Java Swing (included in JDK)
>>>>>>> Stashed changes
