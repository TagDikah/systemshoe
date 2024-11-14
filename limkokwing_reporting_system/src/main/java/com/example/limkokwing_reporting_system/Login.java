package com.example.limkokwing_reporting_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Login {

    @FXML
    private Button btnlogin;

    @FXML
    private Button btnsignup;

    @FXML
    private Label lblpassword;

    @FXML
    private Label lblusername;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private TextField txtusername;

    @FXML
    void login(ActionEvent event) throws IOException {
        String username = txtusername.getText();
        String password = txtpassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please fill in both fields.");
            return;
        }

        try {
            Connection connection = DatabaseConnection.connect();
            if (connection == null) {
                showAlert("Database connection failed.");
                return;
            }

            Statement stmt = connection.createStatement();
            String query = "SELECT role FROM create_account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");

                if ("admin".equals(role)) {
                    Stage myStage = new Stage();
                    String title = "Admin Panel";
                    HelloApplication.nav(myStage, "adminpanel.fxml", title.toUpperCase(), 600, 400, username, role);
                    closeCurrentStage(event);

                } else if ("principal lecturer".equals(role)) {
                    Stage myStage = new Stage();
                    String title = "Principal Lecturer Panel";
                    HelloApplication.nav(myStage, "principal.fxml", title.toUpperCase(), 645, 491, username, role);
                } else {
                    showAlert("You do not have the required access.");
                }

            } else {
                String lecturerQuery = "SELECT * FROM Lecturer WHERE first_name = ? AND lecturer_id = ?";

                PreparedStatement lecturerStatement = connection.prepareStatement(lecturerQuery);
                lecturerStatement.setString(1, username);
                lecturerStatement.setInt(2, Integer.parseInt(password));
                ResultSet lecturerResultSet = lecturerStatement.executeQuery();

                if (lecturerResultSet.next()) {
                    Stage myStage = new Stage();
                    String title = "Lecturer Panel";
                    HelloApplication.nav(myStage, "lecturerpanel.fxml", title.toUpperCase(), 600, 400, username, "lecturer");
                } else {
                    showAlert("Invalid username or password.");
                }
            }

        } catch (SQLException e) {
            showAlert("Error during sign-in: " + e.toString());
        } catch (NumberFormatException e) {
            showAlert("Password must be a number.");
        }
    }

    @FXML
    void signup(ActionEvent event) throws IOException {
        Stage myStage = new Stage();
        String title = "Create Account Form";
        HelloApplication.nav(myStage, "signup.fxml", title.toUpperCase(), 600, 400, "", "");



    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeCurrentStage(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
