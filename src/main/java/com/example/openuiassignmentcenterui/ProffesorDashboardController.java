package com.example.openuiassignmentcenterui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ProffesorDashboardController {

    @FXML
    void checkAssignmentsButtonPressed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("check_assignments.fxml"));
        SceneController scene_switch = new SceneController();
        scene_switch.switchToScene(event,root);
    }

    @FXML
    void setCourseRequirmentsButtonPressed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("set_course_requirements.fxml"));
        SceneController scene_switch = new SceneController();
        scene_switch.switchToScene(event,root);
    }

}
