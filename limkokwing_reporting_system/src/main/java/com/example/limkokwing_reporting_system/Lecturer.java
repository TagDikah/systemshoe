package com.example.limkokwing_reporting_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Lecturer {

    @FXML
    private Button btnadd;

    @FXML
    private Button btnview;

    @FXML
    private ComboBox<String> comboboxrole;

    @FXML
    private Label lblAcademicYear;

    @FXML
    private Label lblClass;

    @FXML
    private Label lblfaculty;

    @FXML
    private Label lblfirstname;

    @FXML
    private Label lbllastname;

    @FXML
    private Label lbllecturerid;

    @FXML
    private Label lblrole;

    @FXML
    private Label lblsemester;

    @FXML
    private TextField txtAcademicYear;

    @FXML
    private TextField txtClass;

    @FXML
    private TextField txtfaculty;

    @FXML
    private TextField txtfirstname;

    @FXML
    private TextField txtlastname;

    @FXML
    private TextField txtlecturerid;

    @FXML
    private TextField txtsemester;



    @FXML
    void add(ActionEvent event) {
        String lecturerId = txtlecturerid.getText();
        String first_name = txtfirstname.getText();
        String last_name = txtlastname.getText();
        String faculty = txtfaculty.getText();
        String role = comboboxrole.getValue(); // Fetch selected role from ComboBox
        String academicYear = txtAcademicYear.getText();
        String class_name = txtClass.getText();
        String semester = txtsemester.getText();

        if (lecturerId.isEmpty() || first_name.isEmpty() || last_name.isEmpty()  ||
                faculty.isEmpty() || role == null || academicYear.isEmpty() || class_name.isEmpty() || semester.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }
        
        try {
            Connection connection = DatabaseConnection.connect();
            String query = "INSERT INTO Lecturer (lecturer_id, first_name, last_name, faculty, role, academic_year, class_name, semester) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1, Integer.parseInt(lecturerId));
            pstmt.setString(2, first_name);
            pstmt.setString(3, last_name);
            pstmt.setString(4, faculty);
            pstmt.setString(5, role);
            pstmt.setString(6, academicYear);
            pstmt.setString(7, class_name);
            pstmt.setString(8, semester);

        int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Lecturer record added successfully!");
                clearFields();
            }

        } catch (SQLException e) {
            showAlert("Error inserting lecturer data: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Invalid input for Lecturer ID or Contacts.");
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
        txtlecturerid.clear();
        txtfirstname.clear();
        txtlastname.clear();
        txtfaculty.clear();
        comboboxrole.setValue(null);
        txtAcademicYear.clear();
        txtClass.clear();
        txtsemester.clear();
    }


        @FXML
        void view(ActionEvent event) {
            StringBuilder result = new StringBuilder();

            try {
                Connection connection = DatabaseConnection.connect();
                String query = "SELECT * FROM lecturer";
                PreparedStatement pstmt = connection.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                while (((ResultSet) rs).next()) {
                    int lecturerId = rs.getInt("lecturer_id");
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String faculty = rs.getString("faculty");
                    String role = rs.getString("role");
                    String academicYear = rs.getString("academic_year");
                    String class_name = rs.getString("class_name");
                    String semester = rs.getString("semester");

                    result.append("Lecturer ID: ").append(lecturerId)
                            .append("\nFirst Name: ").append(first_name)
                            .append("\nLast Name: ").append(last_name)
                            .append("\nFaculty: ").append(faculty)
                            .append("\nRole: ").append(role)
                            .append("\nAcademic Year: ").append(academicYear)
                            .append("\nClass: ").append(class_name)
                            .append("\nSemester: ").append(semester);
                }
                if (!result.isEmpty()) {
                    showAlert(result.toString());
                } else {
                    showAlert("No data found in the lecturer table.");
                }

            } catch (SQLException e) {
                showAlert("Error retrieving lecturer data: " + e.getMessage());
            }
        }

    }

