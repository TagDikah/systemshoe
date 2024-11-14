module com.example.limkokwing_reporting_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.limkokwing_reporting_system to javafx.fxml;
    exports com.example.limkokwing_reporting_system;
}