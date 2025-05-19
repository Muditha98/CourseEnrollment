package view;

import controller.UserController;

import javax.swing.*;
import java.awt.*;

public class RegistrationView extends JFrame {
    private UserController userController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField nameField;
    private JLabel statusLabel;
    
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public RegistrationView(UserController userController) {
        this.userController = userController;
        
        setTitle("Course Enrollment System - Registration");
        setSize(400, 600);
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
        JLabel headerLabel = new JLabel("<html><div style='text-align: center;'>Create New Account</div></html>");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(headerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Registration panel
        JPanel registrationPanel = createStyledPanel();
        registrationPanel.setMaximumSize(new Dimension(300, 350));        // Full Name label and field
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(LABEL_FONT);
        nameLabel.setForeground(PRIMARY_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        registrationPanel.add(nameLabel);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        nameField = createStyledTextField("Enter your full name");
        registrationPanel.add(nameField);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Username label and field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(PRIMARY_COLOR);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        registrationPanel.add(usernameLabel);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        usernameField = createStyledTextField("Choose a username");
        registrationPanel.add(usernameField);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(PRIMARY_COLOR);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        registrationPanel.add(passwordLabel);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        passwordField = createStyledPasswordField("Enter password");
        registrationPanel.add(passwordField);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Confirm Password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(LABEL_FONT);
        confirmPasswordLabel.setForeground(PRIMARY_COLOR);
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        registrationPanel.add(confirmPasswordLabel);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        confirmPasswordField = createStyledPasswordField("Re-enter password");
        registrationPanel.add(confirmPasswordField);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Register button
        JButton registerButton = createStyledButton("Register", new Color(46, 139, 87));
        registerButton.addActionListener(e -> handleRegistration());
        registrationPanel.add(registerButton);
        registrationPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Back to login link
        JButton backButton = createStyledButton("Back to Login", PRIMARY_COLOR);
        backButton.addActionListener(e -> backToLogin());
        registrationPanel.add(backButton);

        mainPanel.add(registrationPanel);

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

    // Reuse the same styled component methods from LoginView
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

    private void handleRegistration() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            return;
        }

        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters long");
            return;
        }

        boolean success = userController.registerUser(username, password, name);
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Registration successful! Please login with your credentials.",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } else {
            statusLabel.setText("Username already exists");
        }
    }

    private void backToLogin() {
        this.dispose();
        new LoginView();
    }
}
