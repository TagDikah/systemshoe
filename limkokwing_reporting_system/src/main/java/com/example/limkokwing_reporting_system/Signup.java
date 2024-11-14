package com.example.limkokwing_reporting_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Signup {

    @FXML
    private Button btncreateaccount;

    @FXML
    private ComboBox<String> comboboxrole;

    @FXML
    private Label lblemail;

    @FXML
    private Label lblname;

    @FXML
    private Label lblpassword;

    @FXML
    private Label lblusername;

    @FXML
    private TextField txtemail;

    @FXML
    private TextField txtname;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private Label txtrole;

    @FXML
    private TextField txtusername;

    @FXML
    void createaccount(ActionEvent event) throws IOException {
        String name = txtname.getText();
        String username = txtusername.getText();
        String email = txtemail.getText();
        String password = txtpassword.getText();
        String role = comboboxrole.getSelectionModel().getSelectedItem();

        try (Connection connection = DatabaseConnection.connect()) {
            if (connection != null) {
                String insertQuery = "INSERT INTO create_account (name, username, email, password, role) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(insertQuery);
                statement.setString(1, name);
                statement.setString(2, username);
                statement.setString(3, email);
                statement.setString(4, password);
                statement.setString(5, role);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Account Created", "Account created successfully!");
                    Stage myStage = new Stage();
                    String title = "login Form";
                    HelloApplication.nav(myStage, "Login.fxml", title.toUpperCase(), 600, 400,"","");
                    closeCurrentStage(event);

                } else {
                    showAlert(Alert.AlertType.ERROR, "Account Creation Failed", "Failed to create account.");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while creating the account: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeCurrentStage(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
}}
