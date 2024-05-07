package controles;

import com.twilio.type.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import javafx.scene.control.TextField;
import services.userservice;
import java.security.SecureRandom;




import java.io.IOException;
import java.sql.SQLException;

public class ForgetPassword {


    @FXML
    private TextField toPhoneNumber;
    @FXML
    private TextField emailTextFeild;
    @FXML
    private Button backbuttton;
    private static final String TWILIO_PHONE_NUMBER = "+14058963517";
    private static final String ACCOUNT_SID = "ACd07d0a2049d678d56d80070215d03cf4";
    private static final String AUTH_TOKEN = "94673931fc3cbd5218fc71e7d019d74f";
    static {
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
    }
    public void sendSmsNotification() throws SQLException {
        String newPassword = generatePassword(8);
        System.out.println(newPassword.toCharArray());
        String messageBody = newPassword;
        userservice userService = new userservice();
        userService.updateUserPassword(emailTextFeild.getText(),newPassword);
        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber.getText()),  // To phone number
                new PhoneNumber(TWILIO_PHONE_NUMBER),  // From Twilio phone number
                messageBody
        ).create();

        System.out.println("Sent message with ID: " + message.getSid());
    }
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";
    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final SecureRandom random = new SecureRandom();
    public static String generatePassword(int length) {
        if (length < 1) throw new IllegalArgumentException("Password length must be at least 1.");

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(PASSWORD_ALLOW_BASE.length());
            char rndChar = PASSWORD_ALLOW_BASE.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }





    @FXML
    public void NavigateToLoginPage(ActionEvent event) {
        try {
            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/User/login.fxml"));
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
