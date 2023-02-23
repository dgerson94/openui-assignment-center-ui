package com.example.openuiassignmentcenterui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainScreenController {

    @FXML
    void professorButtonPressed(ActionEvent event) throws IOException {
        SceneController.switchToScene(event,"professor_sign_in.fxml");
    }

    @FXML
    void studentButtonPressed(ActionEvent event) throws IOException {
        SceneController.switchToScene(event,"student_sign_in.fxml");
    }

}
