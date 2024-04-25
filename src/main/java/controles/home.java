package controles;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import services.userservice;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class home {
    @FXML
    private Label emailLabel;
    @FXML
    private Button editButton;

    private userservice userService = new userservice();  // Ensure userService is initialized appropriately
    public void initialize() {
    }


    public void setEmail(String email) {
        emailLabel.setText(email);
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
            Parent root = loader.load();

            // Get the current stage from any node, here we use the event source
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Set the new scene to stage and show it
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions properly in real applications
        }
    }

    @FXML
    private void deleteAccount(ActionEvent event) {
        String email = emailLabel.getText();  // Retrieve the email from the label
        try {
            userService.deleteUser(email);  // Delete the user
            logout(event);  // Log out or close application after deletion
        } catch (SQLException e) {
            System.err.println("Failed to delete account for email: " + email);
            e.printStackTrace();
            // Optionally show an alert to the user
            showAlert("Delete Account", "Failed to delete the account. Please try again later.");
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void navigateToEdit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/edit.fxml"));
            Parent root = loader.load();
            edit editController = loader.getController();
            editController.setCurrentEmail(emailLabel.getText());  // Pass the current email to the EditController

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception, maybe show an error message
        }
    }

}
