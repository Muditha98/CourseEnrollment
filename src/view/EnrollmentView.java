package view;

import controller.EnrollmentController;
import model.Course;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnrollmentView extends JFrame {
    private Student student;
    private EnrollmentController controller;
    
    private JTable availableCoursesTable;
    private DefaultTableModel tableModel;
    private JButton enrollButton;
    private JButton viewEnrolledButton;
    private JLabel statusLabel;
    
    public EnrollmentView(Student student) {
        this.student = student;
        this.controller = new EnrollmentController();
        
        // Ensure we have sample courses for demonstration
        controller.addSampleCoursesIfNeeded();
        
        setTitle("Course Registration - " + student.getName());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Available Courses for Registration");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        JLabel studentLabel = new JLabel("Student: " + student.getName() + " (ID: " + student.getStudentId() + ")");
        headerPanel.add(studentLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Courses table
        String[] columnNames = {"Course Code", "Title", "Credits", "Available Seats", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        availableCoursesTable = new JTable(tableModel);
        availableCoursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(availableCoursesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        enrollButton = new JButton("Enroll in Selected Course");
        enrollButton.addActionListener(e -> enrollInSelectedCourse());
        buttonPanel.add(enrollButton);
        
        viewEnrolledButton = new JButton("View Enrolled Courses");
        viewEnrolledButton.addActionListener(e -> openEnrolledCoursesView());
        buttonPanel.add(viewEnrolledButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        
        add(mainPanel);
        loadAvailableCourses();
        
        setVisible(true);
    }
    
    private void loadAvailableCourses() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Get all courses
        List<Course> availableCourses = controller.getAllCourses();
        
        // Filter out courses the student is already enrolled in
        List<String> enrolledCourseIds = student.getEnrolledCourseIds();
        
        for (Course course : availableCourses) {
            if (!enrolledCourseIds.contains(course.getCourseId())) {
                Object[] rowData = {
                    course.getCourseCode(),
                    course.getTitle(),
                    course.getCredits(),
                    course.getCapacity() - course.getCurrentEnrollment(),
                    course.getDescription()
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    private void enrollInSelectedCourse() {
        int selectedRow = availableCoursesTable.getSelectedRow();
        if (selectedRow == -1) {
            statusLabel.setText("Please select a course to enroll in.");
            return;
        }
        
        String courseCode = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Find the course
        Course selectedCourse = null;
        for (Course course : controller.getAllCourses()) {
            if (course.getCourseCode().equals(courseCode)) {
                selectedCourse = course;
                break;
            }
        }
        
        if (selectedCourse == null) {
            statusLabel.setText("Course not found.");
            return;
        }
        
        // Enroll student
        boolean success = controller.enrollStudent(student, selectedCourse);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Successfully enrolled in " + selectedCourse.getTitle(),
                "Enrollment Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh the table
            loadAvailableCourses();
        } else {
            statusLabel.setText("Enrollment failed. Course may be full or you are already enrolled.");
        }
    }
    
    private void openEnrolledCoursesView() {
        new EnrolledCoursesView(student, controller);
    }
}