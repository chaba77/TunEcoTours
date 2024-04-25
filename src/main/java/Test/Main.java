//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Test;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/User/login.fxml"));
            if (loader.getLocation() == null) {
                System.out.println("Resource not found!");
            }
            Parent root = (Parent)loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException var5) {
            IOException e = var5;
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
