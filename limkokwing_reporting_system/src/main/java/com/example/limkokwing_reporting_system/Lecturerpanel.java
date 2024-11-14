package com.example.limkokwing_reporting_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Lecturerpanel {

    @FXML
    private Button btnlogout;
    @FXML
    private TextField txtmodeofdelivery;
    @FXML
    private Label lblmodeofdelivery;
    @FXML
    private Button btnsave;

    @FXML
    private DatePicker datePickeronthisday;


    @FXML
    private Text lblonthisday;

    @FXML
    private Text lblstudentattendence;

    @FXML
    private ListView<StudentAttendance> listviewstudentattendence;

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField txtChapter;
    @FXML
    private TextArea textarealearningoutcomes;

    @FXML
    public void initialize() {
        ObservableList<StudentAttendance> students = fetchStudentNames();
        listviewstudentattendence.setItems(students);

        listviewstudentattendence.setCellFactory(lv -> new ListCell<StudentAttendance>() {
            @Override
            protected void updateItem(StudentAttendance item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setSelected(item.isPresent());
                    checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> item.setPresent(isSelected));
                    setGraphic(new HBox(new Label(item.getFullName()), checkBox));
                }
            }
        });
    }

    private ObservableList<StudentAttendance> fetchStudentNames() {
        ObservableList<StudentAttendance> studentAttendances = FXCollections.observableArrayList();
        String query = "SELECT student_id, firstname, lastname FROM student";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                studentAttendances.add(new StudentAttendance(studentId, fullName));
            }

        } catch (SQLException ex) {
            System.out.println("Error retrieving student data: " + ex.getMessage());
        }

        return studentAttendances;
    }
    private void closeCurrentStage(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
    @FXML
    void logout(ActionEvent event) throws IOException {

        Stage myStage = new Stage();
        String title = "login Form";
        HelloApplication.nav(myStage, "Login.fxml", title.toUpperCase(), 600, 400,"","");


    }

    @FXML
    void save(ActionEvent event) {
        LocalDate attendanceDate = datePickeronthisday.getValue();
        String chapter = txtChapter.getText();
        String learningOutcomes = textarealearningoutcomes.getText();
        String modeofdelivery= txtmodeofdelivery.getText();

        if (attendanceDate == null || chapter.isEmpty() || learningOutcomes.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        String insertQuery = "INSERT INTO attendance (student_id, attendance_date, chapter, learning_outcomes, is_present,mode_of_delivery) VALUES (?, ?, ?, ?, ?,?)";

        try (Connection connection = DatabaseConnection.connect()) {
            for (StudentAttendance student : listviewstudentattendence.getItems()) {
                try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                    pstmt.setInt(1, student.getStudentId());
                    pstmt.setDate(2, java.sql.Date.valueOf(attendanceDate));
                    pstmt.setString(3, chapter);
                    pstmt.setString(4, learningOutcomes);
                    pstmt.setBoolean(5, student.isPresent());
                    pstmt.setString(6, modeofdelivery);

                    pstmt.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println("Error saving attendance for student ID " + student.getStudentId() + ": " + ex.getMessage());
                }
            }

            System.out.println("Attendance records saved successfully.");

        } catch (SQLException ex) {
            System.out.println("Error saving attendance: " + ex.getMessage());
        }
    }
}

