module com.example.openuiassignmentcenterui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.openuiassignmentcenterui to javafx.fxml;
    exports com.example.openuiassignmentcenterui;
    exports com.example.openuiassignmentcenterui.controllers;
    opens com.example.openuiassignmentcenterui.controllers to javafx.fxml;
}