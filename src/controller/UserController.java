package controller;

import model.User;
import model.Student;
import util.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserController {
    private static final String USERS_FILE = "data/users.txt";
    private List<User> users;

    public UserController() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<String> lines = FileHandler.readFile(USERS_FILE);
            users = new ArrayList<>();
            for (String line : lines) {
                users.add(User.fromString(line));
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            users = new ArrayList<>();
        }
    }

    private void saveUsers() {
        try {
            List<String> lines = new ArrayList<>();
            for (User user : users) {
                lines.add(user.toString());
            }
            FileHandler.writeFile(USERS_FILE, lines);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public boolean registerUser(String username, String password, String name) {
        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }

        // Create new student and user
        String studentId = "S" + String.format("%03d", users.size() + 1);
        Student student = new Student(studentId, name);
        User user = new User(username, password, "student", student);
        users.add(user);
        saveUsers();
        return true;
    }
}
