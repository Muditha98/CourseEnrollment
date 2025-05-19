package view;

import controller.EnrollmentController;
import model.Course;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
    
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255); // Alice Blue
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    public EnrolledCoursesView(Student student, EnrollmentController controller) {
        this.student = student;
        this.controller = controller;
        
        setTitle("Enrolled Courses");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set window background
        getContentPane().setBackground(SECONDARY_COLOR);
        
        // Create main panel
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
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Styled Header Label
        JLabel headerLabel = new JLabel("<html><div style='text-align: center; font-size: 24px; color: white;'>" +
                "Your Enrolled Courses</div></html>");
        headerLabel.setFont(HEADER_FONT);
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        // Styled Student Info
        JLabel studentLabel = new JLabel("<html><div style='text-align: right; color: white;'>" +
                "Student: <b>" + student.getName() + "</b><br>ID: " + student.getStudentId() + "</div></html>");
        studentLabel.setFont(LABEL_FONT);
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
        enrolledCoursesTable.setFont(LABEL_FONT);
        enrolledCoursesTable.setRowHeight(30);
        enrolledCoursesTable.setSelectionBackground(PRIMARY_COLOR);
        enrolledCoursesTable.setSelectionForeground(Color.WHITE);
        enrolledCoursesTable.setShowGrid(false);
        enrolledCoursesTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Style table header
        JTableHeader header = enrolledCoursesTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Add zebra striping to table
        enrolledCoursesTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
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
        
        JScrollPane scrollPane = new JScrollPane(enrolledCoursesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(SECONDARY_COLOR);
        
        dropButton = createStyledButton("Drop Selected Course", new Color(220, 20, 60)); // Crimson red
        dropButton.addActionListener(e -> dropSelectedCourse());
        buttonPanel.add(dropButton);
        
        backButton = createStyledButton("Back to Registration", PRIMARY_COLOR);
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(new Color(220, 20, 60)); // Crimson red
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(SECONDARY_COLOR);
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        
        add(mainPanel);
        loadEnrolledCourses();
        
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