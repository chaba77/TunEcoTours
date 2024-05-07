module TunEcoTours {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires bcrypt;
    requires com.google.gson;
    requires java.desktop;

    opens Test to javafx.fxml, javafx.graphics;
    exports controles to javafx.fxml;
    opens controles to javafx.fxml;
    opens Enntities to javafx.base;
    exports com.example.tunecotours;
    requires twilio;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires javafx.web;


}