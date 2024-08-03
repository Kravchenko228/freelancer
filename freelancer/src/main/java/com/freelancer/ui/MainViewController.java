package com.freelancer.ui;

import com.freelancer.Business;
import com.freelancer.BusinessService;
import com.freelancer.User;
import com.freelancer.UserService;
import com.freelancer.dao.UserRepository;
import com.freelancer.dao.BusinessRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.Optional;

public class MainViewController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private UserService userService;
    private User currentUser;

    public MainViewController() {
        this.userService = new UserService(new UserRepository("users.dat"));
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please enter both username and password.");
            return;
        }

        Optional<User> optionalUser = userService.login(username, password);
        if (optionalUser.isPresent()) {
            currentUser = optionalUser.get();
            openBusinessView(false);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password.");
        }
    }
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RegisterView.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Failed to open the registration form.");
        }
    }


    @FXML
    private void handleGuest() {
        openBusinessView(true);
    }

    private void openBusinessView(boolean isGuest) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BusinessView.fxml"));
            Parent root = fxmlLoader.load();
            BusinessViewController controller = fxmlLoader.getController();
            controller.setGuest(isGuest);
            controller.setCurrentUser(currentUser);
            Stage stage = new Stage();
            controller.setStageTitle(stage, "Freelancer");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
