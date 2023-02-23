module com.example.openuiassignmentcenterui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;

    opens com.example.openuiassignmentcenterui to javafx.fxml;
    exports com.example.openuiassignmentcenterui;
    exports com.example.openuiassignmentcenterui.controllers;
    opens com.example.openuiassignmentcenterui.controllers to javafx.fxml;
    exports com.example.openuiassignmentcenterui.helpers;
    opens com.example.openuiassignmentcenterui.helpers to javafx.fxml;

    exports com.example.openuiassignmentcenterui.models to com.google.gson;
    opens com.example.openuiassignmentcenterui.models to com.google.gson;

}