package com.example.limkokwing_reporting_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class academicComponents {

    @FXML
    private Button btnadd;

    @FXML
    private Button btnview;

    @FXML
    private ComboBox<String> comboboxsemester;

    @FXML
    private Label lblacamedicyear;

    @FXML
    private Label lblclass;

    @FXML
    private Label lblmodule;

    @FXML
    private Label lblsemester;

    @FXML
    private TextField txtclass;

    @FXML
    private TextField txtmodule;

    @FXML
    private TextField txtacademicyear;


    @FXML
    void add(ActionEvent event) {
        // Retrieve academic year value from txtacademicyear
        String academicYear = txtacademicyear.getText();
        String className = txtclass.getText();
        String semester = comboboxsemester.getValue();
        String module = txtmodule.getText();

        // Check if any field is empty
        if (academicYear.isEmpty() ||className.isEmpty() || semester.isEmpty()  || module.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            // Connect to the database and execute insert statement
            Connection connection = DatabaseConnection.connect();
            String query = "INSERT INTO academic_details (academic_year, class, semester, module) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, academicYear);
            pstmt.setString(2, className);
            pstmt.setString(3, semester);
            pstmt.setString(4, module);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Academic record added successfully!");
                clearFields();
            }

        } catch (SQLException e) {
            showAlert("Error inserting academic data: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        comboboxsemester.setValue(null);  // Clear semester ComboBox
        txtclass.clear();  // Clear class TextField
        txtmodule.clear(); // Clear module TextField
        txtacademicyear.clear();  // Clear academic year TextField
    }

    @FXML
    void view(ActionEvent event) {
        try {
            Connection connection = DatabaseConnection.connect();
            String query = "SELECT * FROM academic_details";
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet resultSet = pstmt.executeQuery();

            StringBuilder data = new StringBuilder("Academic Details:\n");

            while (resultSet.next()) {
                String academicYear = resultSet.getString("academic_year");
                String className = resultSet.getString("class");
                String semester = resultSet.getString("semester");
                String module = resultSet.getString("module");

                data.append("Academic Year: ").append(academicYear).append("\n")
                        .append("Class: ").append(className).append("\n")
                        .append("Semester: ").append(semester).append("\n")
                        .append("Module: ").append(module).append("\n\n");
            }

            showAlert(data.toString());

        } catch (SQLException e) {
            showAlert("Error retrieving academic data: " + e.getMessage());
        }
    }
}
