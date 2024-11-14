package com.example.limkokwing_reporting_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HelloApplication.nav(stage, "Login.fxml", "LOGIN FORM", 600, 400, "", "");
    }

    public static void nav(Stage stage, String fxml, String title, int width, int height, String adminName, String role) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        Parent root = loader.load();

        if (fxml.equals("adminpanel.fxml")) {
            Adminpanel controller = loader.getController();
            controller.setAdminDetails(adminName, role);
        }

        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.show();
    }
}
