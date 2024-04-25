package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.userservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import Enntities.User;

public class register {
    @FXML
    private TextField Email;

    @FXML
    private PasswordField PasswordText;

    @FXML
    private Button registerButton;
    private userservice userService;
    public register() {
        userService = new userservice(); // Initialize userservice
    }
// Inject userservice

    void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    boolean isValidEmail(String email) {
        // Simple email validation regex pattern
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    @FXML
    void Register(ActionEvent event) {

        if (!isValidEmail(Email.getText())) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Invalid email address. Please provide a valid email.");
            return; // Exit method if email is invalid
        }

        try {
            if (userService.emailExists(Email.getText())) {
                showAlert(Alert.AlertType.WARNING, "Email Exists", "Email address already exists. Please choose a different email.");
                return; // Exit method if email already exists
            }

            User newUser = new User(Email.getText(), PasswordText.getText(),Arrays.asList("ROLE_CLIENT"),0);
            System.out.println("User roles: " + newUser.getRoles());
            String rolesString = String.join(",", newUser.getRoles());
            System.out.println(rolesString);
            userService.create(newUser);

            // Navigate to the login page
            NavigateToLoginPage(event);
        } catch (SQLException e) {
            // Provide meaningful feedback to the user
            System.err.println("Registration failed. Please try again later.");
            e.printStackTrace();
        }
    }
    @FXML
    void NavigateToLoginPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/User/login.fxml"));
            Scene scene = registerButton.getScene();
            if (scene != null) {
                scene.setRoot(root);
            }
            Stage stage = (Stage) scene.getWindow();
            //new ZoomIn(root).play();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
