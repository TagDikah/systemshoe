package com.example.limkokwing_reporting_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Principal {
    @FXML
    private Button btnlogout;

    @FXML
    private Button btnsubmit;

    @FXML
    private Button btnviewreports;

    @FXML
    private DatePicker datepickeronthisdate;

    @FXML
    private Label lblModulecode;

    @FXML
    private Label lblchallenges;

    @FXML
    private Label lblchapter;

    @FXML
    private Label lblclass;

    @FXML
    private Label lblduration;

    @FXML
    private Label lblfaculty;

    @FXML
    private Label lblmoduleandtitle;

    @FXML
    private Label lblname;

    @FXML
    private Label lblonthisdate;

    @FXML
    private Label lblrecommendations;

    @FXML
    private TextArea textareachallenges;

    @FXML
    private TextArea textarearecommendations;

    @FXML
    private TextField txtModulecode;

    @FXML
    private TextField txtchapter;

    @FXML
    private TextField txtclass;

    @FXML
    private TextField txtduration;

    @FXML
    private TextField txtfaculty;

    @FXML
    private TextField txtmoduleandtitle;

    @FXML
    private TextField txtname;

    @FXML
    void submit(ActionEvent event) {
        String faculty = txtfaculty.getText();
        String name = txtname.getText();
        String className = txtclass.getText();
        String chapter = txtchapter.getText();
        String moduleTitle = txtmoduleandtitle.getText();
        String moduleCode = txtModulecode.getText();
        String duration = txtduration.getText();
        String recommendations = textarearecommendations.getText();
        String challenges = textareachallenges.getText();
        LocalDate date = datepickeronthisdate.getValue();

        if (faculty.isEmpty() || name.isEmpty() || className.isEmpty() || chapter.isEmpty() ||
                moduleTitle.isEmpty() || moduleCode.isEmpty() || duration.isEmpty() || date == null) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            Connection connection = DatabaseConnection.connect();
            String query = "INSERT INTO principal_lecturer (faculty, name, class, chapter, module_title, module_code, duration, recommendations, challenges, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, faculty);
            pstmt.setString(2, name);
            pstmt.setString(3, className);
            pstmt.setString(4, chapter);
            pstmt.setString(5, moduleTitle);
            pstmt.setString(6, moduleCode);
            pstmt.setString(7, duration);
            pstmt.setString(8, recommendations);
            pstmt.setString(9, challenges);
            pstmt.setDate(10, java.sql.Date.valueOf(date));

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Record added successfully!");
                clearFields();
            }

            pstmt.close();
            connection.close();

        } catch (SQLException e) {
            showAlert("Error inserting data: " + e.getMessage());
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
        txtfaculty.clear();
        txtname.clear();
        txtclass.clear();
        txtchapter.clear();
        txtmoduleandtitle.clear();
        txtModulecode.clear();
        txtduration.clear();
        textarearecommendations.clear();
        textareachallenges.clear();
        datepickeronthisdate.setValue(null);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Stage myStage = new Stage();
        String title = "Login form";
        HelloApplication.nav(myStage, "Login.fxml", title.toUpperCase(), 600, 400,"","");
    }


    @FXML
    void viewreports(ActionEvent event) {
        try {
            Connection connection = DatabaseConnection.connect();

            String lecturerQuery = "SELECT name, class, module_title, chapter, recommendations, challenges FROM principal_lecturer";
            PreparedStatement lecturerStmt = connection.prepareStatement(lecturerQuery);
            ResultSet lecturerRs = lecturerStmt.executeQuery();

            String attendanceQuery = "SELECT mode_of_delivery, COUNT(*) AS total_students, SUM(is_present) AS present_students FROM attendance GROUP BY mode_of_delivery";
            PreparedStatement attendanceStmt = connection.prepareStatement(attendanceQuery);
            ResultSet attendanceRs = attendanceStmt.executeQuery();

            if (lecturerRs.next() && attendanceRs.next()) {
                String name = lecturerRs.getString("name");
                String className = lecturerRs.getString("class");
                String moduleTitle = lecturerRs.getString("module_title");
                String chapter = lecturerRs.getString("chapter");
                String recommendations = lecturerRs.getString("recommendations");
                String challenges = lecturerRs.getString("challenges");

                String modeOfDelivery = attendanceRs.getString("mode_of_delivery");
                int totalStudents = attendanceRs.getInt("total_students");
                int presentStudents = attendanceRs.getInt("present_students");
                int absentStudents = totalStudents - presentStudents;

                String message = String.format(
                        "Name: %s\nClass: %s\nModule Title: %s\nChapter: %s\n" +
                                "Mode of Delivery: %s\nTotal Students: %d\nPresent Students: %d\nAbsent Students: %d\n" +
                                "Recommendations: %s\nChallenges: %s",
                        name, className, moduleTitle, chapter, modeOfDelivery, totalStudents, presentStudents, absentStudents,
                        recommendations, challenges
                );

                showAlert(message);
            } else {
                showAlert("No report data found.");
            }

            lecturerRs.close();
            lecturerStmt.close();
            attendanceRs.close();
            attendanceStmt.close();
            connection.close();


        } catch (SQLException e) {
            showAlert("Error retrieving data: " + e.getMessage());
        }
    }}
