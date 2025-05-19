package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private UserController userController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public LoginView() {
        this.userController = new UserController();
        
        setTitle("Course Enrollment System - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, SECONDARY_COLOR, 0, h, new Color(220, 230, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JLabel headerLabel = new JLabel("<html><div style='text-align: center;'>Welcome to<br>Course Enrollment System</div></html>");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(headerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Login panel
        JPanel loginPanel = createStyledPanel();
        loginPanel.setMaximumSize(new Dimension(300, 250));        // Username label and field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(PRIMARY_COLOR);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(usernameLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        usernameField = createStyledTextField("Enter username");
        loginPanel.add(usernameField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(PRIMARY_COLOR);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(passwordLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        passwordField = createStyledPasswordField("Enter password");
        loginPanel.add(passwordField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Login button
        JButton loginButton = createStyledButton("Login", PRIMARY_COLOR);
        loginButton.addActionListener(e -> handleLogin());
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Register link
        JButton registerButton = createStyledButton("Create New Account", new Color(46, 139, 87));
        registerButton.addActionListener(e -> openRegistrationView());
        loginPanel.add(registerButton);

        mainPanel.add(loginPanel);

        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(statusLabel);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setFont(LABEL_FONT);
        field.setMaximumSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(15);
        field.setFont(LABEL_FONT);
        field.setMaximumSize(new Dimension(300, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(300, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return;
        }

        User user = userController.authenticate(username, password);
        if (user != null) {
            // Login successful
            this.dispose();
            new EnrollmentView(user.getStudent());
        } else {
            statusLabel.setText("Invalid username or password");
        }
    }

    private void openRegistrationView() {
        this.dispose();
        new RegistrationView(userController);
    }
}
