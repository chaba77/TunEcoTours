package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.userservice;

import java.io.IOException;

public class editBack {
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
    public void NavigateToBackPage(ActionEvent event) {
        try {
            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/User/Back.fxml"));
            Scene scene = this.backbuttton.getScene();
            if (scene != null) {
                scene.setRoot(root);
            }

            Stage var4 = (Stage)scene.getWindow();
        } catch (IOException var5) {
            IOException e = var5;
            System.err.println(e.getMessage());
        }

    }
}
