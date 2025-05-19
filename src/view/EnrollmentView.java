package view;

import controller.EnrollmentController;
import model.Course;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
    
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255); // Alice Blue
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    public EnrollmentView(Student student) {
        this.student = student;
        this.controller = new EnrollmentController();
        
        // Ensure we have sample courses for demonstration
        controller.addSampleCoursesIfNeeded();
        
        setTitle("Course Registration System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set window background
        getContentPane().setBackground(SECONDARY_COLOR);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Header Panel with gradient
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, w, h, new Color(100, 149, 237));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Styled Header Label
        JLabel headerLabel = new JLabel("<html><div style='text-align: center; font-size: 24px; color: white;'>" +
                "Available Courses for Registration</div></html>");
        headerLabel.setFont(HEADER_FONT);
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        // Styled Student Info
        JLabel studentLabel = new JLabel("<html><div style='text-align: right; color: white;'>" +
                "Student: <b>" + student.getName() + "</b><br>ID: " + student.getStudentId() + "</div></html>");
        studentLabel.setFont(LABEL_FONT);
        headerPanel.add(studentLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Courses table with custom styling
        String[] columnNames = {"Course Code", "Title", "Credits", "Available Seats", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        availableCoursesTable = new JTable(tableModel);
        availableCoursesTable.setFont(LABEL_FONT);
        availableCoursesTable.setRowHeight(30);
        availableCoursesTable.setSelectionBackground(PRIMARY_COLOR);
        availableCoursesTable.setSelectionForeground(Color.WHITE);
        availableCoursesTable.setShowGrid(false);
        availableCoursesTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Style table header
        JTableHeader header = availableCoursesTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Add zebra striping to table
        availableCoursesTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 255));
                }
                return c;
            }
        });
        
        // Wrap table in scrollPane with custom border
        JScrollPane scrollPane = new JScrollPane(availableCoursesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(SECONDARY_COLOR);
        
        enrollButton = createStyledButton("Enroll in Selected Course", new Color(46, 139, 87));
        enrollButton.addActionListener(e -> enrollInSelectedCourse());
        buttonPanel.add(enrollButton);
        
        viewEnrolledButton = createStyledButton("View Enrolled Courses", PRIMARY_COLOR);
        viewEnrolledButton.addActionListener(e -> openEnrolledCoursesView());
        buttonPanel.add(viewEnrolledButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Status label with HTML styling
        statusLabel = new JLabel("");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(new Color(220, 20, 60)); // Crimson red
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        
        add(mainPanel);
        loadAvailableCourses();
        
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
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