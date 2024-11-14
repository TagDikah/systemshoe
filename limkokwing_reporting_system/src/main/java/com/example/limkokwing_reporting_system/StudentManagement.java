package com.example.limkokwing_reporting_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class StudentManagement {

    @FXML
    private Button btnadd;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnview;

    @FXML
    private ChoiceBox<String> choiceboxgender;

    @FXML
    private Label lblclass;

    @FXML
    private Label lblcontacts;

    @FXML
    private Label lblfirstname;

    @FXML
    private Label lblgender;

    @FXML
    private Label lbllastname;

    @FXML
    private Label lblstudentid;

    @FXML
    private TextField txtclass;

    @FXML
    private TextField txtcontacts;

    @FXML
    private TextField txtfirstname;

    @FXML
    private TextField txtlastname;

    @FXML
    private TextField txtstudentid;

    @FXML
    void add(ActionEvent event) {
        String studentId = txtstudentid.getText();
        String firstName = txtfirstname.getText();
        String lastName = txtlastname.getText();
        String contacts = txtcontacts.getText();
        String gender = choiceboxgender.getValue();
        String className = txtclass.getText();

        if (studentId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || contacts.isEmpty() || gender == null || className.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            Connection connection = DatabaseConnection.connect();
            String query = "INSERT INTO student (Student_id, firstname, lastname, contacts, gender, class) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(studentId));
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setLong(4, Long.parseLong(contacts));
            pstmt.setString(5, gender);
            pstmt.setString(6, className);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Student record added successfully!");
                clearFields();
            }

        } catch (SQLException e) {
            showAlert("Error inserting student data: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Invalid input for Student ID or Contacts.");
        }
    }

    @FXML
    void delete(ActionEvent event) {
        String studentId = txtstudentid.getText();

        if (studentId.isEmpty()) {
            showAlert("Please enter the Student ID to delete.");
            return;
        }

        try {
            Connection connection = DatabaseConnection.connect();
            String query = "DELETE FROM student WHERE Student_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(studentId));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Student record deleted successfully.");
                clearFields();
            } else {
                showAlert("No student found with ID: " + studentId);
            }

        } catch (SQLException e) {
            showAlert("Error deleting student: " + e.toString());
        } catch (NumberFormatException e) {
            showAlert("Invalid Student ID format. Please enter a numeric value.");
        }
    }

    @FXML
    void view(ActionEvent event) {
        StringBuilder studentData = new StringBuilder();

        try (Connection connection = DatabaseConnection.connect()) {
            if (connection != null) {
                String query = "SELECT * FROM student";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int studentId = resultSet.getInt("Student_id");
                    String firstName = resultSet.getString("firstname");
                    String lastName = resultSet.getString("lastname");
                    String contacts = resultSet.getString("contacts");
                    String gender = resultSet.getString("gender");
                    String className = resultSet.getString("class");

                    studentData.append("ID: ").append(studentId)
                            .append(", Name: ").append(firstName).append(" ").append(lastName)
                            .append(", Contacts: ").append(contacts)
                            .append(", Gender: ").append(gender)
                            .append(", Class: ").append(className)
                            .append("\n");
                }

                if (!studentData.isEmpty()) {
                    showAlert("Student Records:\n" + studentData.toString());
                } else {
                    showAlert("No student records found.");
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            showAlert("Error retrieving student data: " + e.getMessage());
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
        txtstudentid.clear();
        txtfirstname.clear();
        txtlastname.clear();
        txtcontacts.clear();
        txtclass.clear();
        choiceboxgender.setValue(null);
    }
}
