package controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import services.userservice;

import java.io.IOException;


public class edit {
    @FXML
    private TextField emailTextField;
    @FXML
    private Button backbuttton;

    private userservice userService = new userservice();
    private String currentEmail;  // This should be set when opening the edit view

    public void setCurrentEmail(String email) {
        currentEmail = email;
        emailTextField.setText(email);
    }

    @FXML
    private void updateEmail() {
        String newEmail = emailTextField.getText();
        try {
            userService.updateUserEmail(currentEmail, newEmail);
            // Optionally close the window or navigate away
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error, show alert
        }
    }
    @FXML
    public void NavigateToHomePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/home.fxml"));
            Parent root = loader.load();
            home homeController = loader.getController();

            if (homeController == null) {
                throw new RuntimeException("Controller could not be loaded.");
            }

            homeController.setEmail(emailTextField.getText());

            Scene scene = backbuttton.getScene();
            if (scene != null) {
                scene.setRoot(root);
                Stage stage = (Stage) scene.getWindow();
                stage.setScene(scene);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error navigating to the home page: " + e.getMessage());
        }
    }


}
