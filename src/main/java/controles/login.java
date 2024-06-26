package controles;

import java.io.IOException;
import java.sql.SQLException;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.userSession;
import services.userservice;
import Enntities.User;
import controles.home;

public class login {
    @FXML
    private TextField Email;
    @FXML
    private PasswordField PasswordText;
    @FXML
    private Button LoginButton;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button ForgetPasswordButton;

    @FXML
    private WebView captcha;

    @FXML
    public void NavigateToRegisterPage(ActionEvent event) {
        try {
            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/User/register.fxml"));
            Scene scene = this.RegisterButton.getScene();
            if (scene != null) {
                scene.setRoot(root);
            }

            Stage var4 = (Stage)scene.getWindow();
        } catch (IOException var5) {
            IOException e = var5;
            System.err.println(e.getMessage());
        }

    }
    void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void Login(ActionEvent event) {
        String email = Email.getText();
        String password = PasswordText.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter both username and password.");
            return;
        }
        try{
            WebEngine engine = captcha.getEngine();
            String result = (String) engine.executeScript(
                    "function isRecaptchaVerified() {" +
                            " var isVerified = grecaptcha.getResponse().length > 0;" +
                            " return String(isVerified);" +
                            "} " +
                            "isRecaptchaVerified();"
            );
            if(result.equals("false")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("captcha");
                alert.setContentText("Please verify the captcha.");
                alert.showAndWait();
                System.out.println("erreur");
                return;

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            userservice userService = new userservice();
            if (userService.authenticate(email, password)) {
                User currentUser = userService.getUserByEmail(email);
                userSession.setCurrentUser(currentUser);

                FXMLLoader loader = new FXMLLoader();
                if (email.equals("admin@admin.de")) {
                    loader.setLocation(getClass().getResource("/User/Back.fxml"));
                } else {
                    loader.setLocation(getClass().getResource("/User/home.fxml"));

                }
                Parent root = loader.load();

                // Avoid class casting, use BaseController or similar approach if needed
                if (!email.equals("admin@admin.de")) {
                    home homeController = loader.getController();
                    homeController.setEmail(email);
                }

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid email or password.");
            }
        } catch (IOException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Authentication Error", "An error occurred during authentication.");
            e.printStackTrace();
        }
    }
    @FXML
    public void NavigateToForgetPasswordPage(ActionEvent event) {
        try {
            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/User/forgetpassword.fxml"));
            Scene scene = this.ForgetPasswordButton.getScene();
            if (scene != null) {
                scene.setRoot(root);
            }

            Stage var4 = (Stage)scene.getWindow();
        } catch (IOException var5) {
            IOException e = var5;
            System.err.println(e.getMessage());
        }

    }


    public void initialize()  {
        captcha.getEngine().load("http://localhost/captcha.html");

        // Add hover animation
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), captcha);
        scaleTransition.setToX(2); // Scale horizontally by 2x
        scaleTransition.setToY(2); // Scale vertically by 2x

        captcha.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            // On hover, play the animation
            scaleTransition.playFromStart();
        });

        captcha.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            // On exit, reverse the animation
            scaleTransition.setToX(1); // Scale horizontally by 2x
            scaleTransition.setToY(1);
            scaleTransition.play();
        });


    }


}
