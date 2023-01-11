package com.example.openuiassignmentcenterui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class MainScreenController {

    @FXML
    void proffesorButtonPressed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("proffesor_sign_in.fxml"));
        SceneController scene_switch = new SceneController();
        scene_switch.switchToScene(event,root);
    }

    @FXML
    void studentButtonPressed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("student_sign_in.fxml"));
        SceneController scene_switch = new SceneController();
        scene_switch.switchToScene(event,root);
    }

}
