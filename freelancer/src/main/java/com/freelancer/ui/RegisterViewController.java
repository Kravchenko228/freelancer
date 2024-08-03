package com.freelancer.ui;

import com.freelancer.User;
import com.freelancer.UserService;
import com.freelancer.dao.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private UserService userService;

    @FXML
    private TextField confirmPasswordField;

    public RegisterViewController() {
        this.userService = new UserService(new UserRepository("users.dat"));
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Passwords do not match.");
            return;
        }

        try {
            User newUser = new User(username, password);
            userService.register(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Registration Success", "User registered successfully.");
            closeWindow();
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
