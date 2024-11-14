package com.example.limkokwing_reporting_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Adminpanel {

    @FXML
    private Label lblAdminName;

    @FXML
    private Label lblAdminRole;

    @FXML
    private Button btnacademiccomponents;

    @FXML
    private Button btnlecturermanagement;

    @FXML
    private Button btnlogout;

    @FXML
    private Button btnstudentmanagement;

    @FXML
    private Button btnviewanalytics;

    public void setAdminDetails(String name, String role) {
        lblAdminName.setText(name);
        lblAdminRole.setText(role);
    }

    @FXML
    void academiccomponents(ActionEvent event) throws IOException {
        Stage myStage = new Stage();
        String title = "Academic Details";
        HelloApplication.nav(myStage, "academicComponents.fxml", title.toUpperCase(), 600, 400, "", "");

    }

    @FXML
    void lecturermanagement(ActionEvent event) throws IOException {
        Stage myStage = new Stage();
        String title = "Lecturer Form";
        HelloApplication.nav(myStage, "lecturer.fxml", title.toUpperCase(), 600, 400, "", "");

    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Stage myStage = new Stage();
        String title = "Login Form";
        HelloApplication.nav(myStage, "login.fxml", title.toUpperCase(), 600, 400, "", "");
    }

    @FXML
    void studentmanagement(ActionEvent event) throws IOException {
        Stage myStage = new Stage();
        String title = "Student Details";
        HelloApplication.nav(myStage, "studentManagement.fxml", title.toUpperCase(), 600, 400, "", "");

    }

    @FXML
    void viewanalytics(ActionEvent event) throws IOException {
        Stage myStage = new Stage();
        String title = "School Performance";
        HelloApplication.nav(myStage, "viewAnalytics.fxml", title.toUpperCase(), 600, 400, "", "");

    }

    @FXML
    void viewprofile(ActionEvent event) throws IOException {
        try {
            Connection connection = DatabaseConnection.connect();
            if (connection == null) {
                showAlert("Database connection failed.");
                return;
            }

            String query = "SELECT name, username, email, password, role, last_login FROM create_account WHERE role = 'admin'";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            if (resultSet.next()) {
                // Retrieve admin details
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String lastLogin = resultSet.getString("last_login");

                showAlert("Admin Profile:\n\n" +
                        "Name: " + name + "\n" +
                        "Username: " + username + "\n" +
                        "Email: " + email + "\n" +
                        "Password: " + password + "\n" +
                        "Role: " + role + "\n" +
                        "Last Login: " + lastLogin);
            } else {
                showAlert("No admin profile found.");
            }

            resultSet.close();
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            showAlert("Error retrieving profile: " + e.toString());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Profile Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeCurrentStage(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
