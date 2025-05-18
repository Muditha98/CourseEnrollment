package view;

import controller.EnrollmentController;
import model.Course;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnrolledCoursesView extends JFrame {
    private Student student;
    private EnrollmentController controller;
    
    private JTable enrolledCoursesTable;
    private DefaultTableModel tableModel;
    private JButton dropButton;
    private JButton backButton;
    private JLabel statusLabel;
    
    public EnrolledCoursesView(Student student, EnrollmentController controller) {
        this.student = student;
        this.controller = controller;
        
        setTitle("Enrolled Courses - " + student.getName());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Your Enrolled Courses");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        JLabel studentLabel = new JLabel("Student: " + student.getName() + " (ID: " + student.getStudentId() + ")");
        headerPanel.add(studentLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Courses table
        String[] columnNames = {"Course Code", "Title", "Credits", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        enrolledCoursesTable = new JTable(tableModel);
        enrolledCoursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(enrolledCoursesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        dropButton = new JButton("Drop Selected Course");
        dropButton.addActionListener(e -> dropSelectedCourse());
        buttonPanel.add(dropButton);
        
        backButton = new JButton("Back to Registration");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        
        add(mainPanel);
        loadEnrolledCourses();
        
        setVisible(true);
    }
    
    private void loadEnrolledCourses() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Get enrolled courses
        List<Course> enrolledCourses = controller.getEnrolledCourses(student);
        
        for (Course course : enrolledCourses) {
            Object[] rowData = {
                course.getCourseCode(),
                course.getTitle(),
                course.getCredits(),
                course.getDescription()
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void dropSelectedCourse() {
        int selectedRow = enrolledCoursesTable.getSelectedRow();
        if (selectedRow == -1) {
            statusLabel.setText("Please select a course to drop.");
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
        
        // Confirm drop
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to drop " + selectedCourse.getTitle() + "?",
            "Confirm Drop",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Drop course
            boolean success = controller.dropCourse(student, selectedCourse);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Successfully dropped " + selectedCourse.getTitle(),
                    "Course Dropped",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the table
                loadEnrolledCourses();
            } else {
                statusLabel.setText("Failed to drop course.");
            }
        }
    }
}