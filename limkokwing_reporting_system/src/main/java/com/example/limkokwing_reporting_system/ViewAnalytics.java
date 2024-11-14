package com.example.limkokwing_reporting_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAnalytics {

    @FXML
    private Button btnlogout;

    @FXML
    private Button btnviewlecturer;

    @FXML
    private Button btnviewstudents;

    @FXML
    private PieChart lecturerPieChart;

    @FXML
    private BarChart<String, Number> studentBarChart;

    private void closeCurrentStage(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Stage myStage = new Stage();
        String title = "Login form";
        HelloApplication.nav(myStage, "Login.fxml", title.toUpperCase(), 600, 400,"","");
        closeCurrentStage(event);
    }

    @FXML
    void viewlecturer(ActionEvent event) {
        loadLecturerData();
    }

    private void loadLecturerData() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT faculty, COUNT(*) AS lecturer_count FROM lecturer GROUP BY faculty";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear any previous data from the PieChart
            lecturerPieChart.getData().clear();

            // Calculate the total number of lecturers
            int totalLecturers = 0;
            while (resultSet.next()) {
                totalLecturers += resultSet.getInt("lecturer_count");
            }

            // Close the previous ResultSet and re-run the query for accurate calculation
            resultSet.close();
            resultSet = statement.executeQuery();

            // Add data to the pie chart
            while (resultSet.next()) {
                String faculty = resultSet.getString("faculty");
                int count = resultSet.getInt("lecturer_count");
                double percentage = ((double) count / totalLecturers) * 100;

                PieChart.Data slice = new PieChart.Data(faculty + " (" + String.format("%.1f", percentage) + "%)", count);
                lecturerPieChart.getData().add(slice);
            }

        } catch (SQLException e) {
            showAlert("Error loading lecturer data: " + e.getMessage());
        }
    }

    @FXML
    void viewstudents(ActionEvent event) {
        loadStudentData();
    }

    private void loadStudentData() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT class, COUNT(*) AS student_count FROM student GROUP BY class";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            XYChart.Series<String, Number> studentSeries = new XYChart.Series<>();
            studentSeries.setName("Student Count by Class");

            while (resultSet.next()) {
                String studentClass = resultSet.getString("class");
                int studentCount = resultSet.getInt("student_count");

                studentSeries.getData().add(new XYChart.Data<>(studentClass, studentCount));
            }

            studentBarChart.getData().clear();
            studentBarChart.getData().add(studentSeries);

        } catch (SQLException e) {
            showAlert("Error loading bar chart data: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
