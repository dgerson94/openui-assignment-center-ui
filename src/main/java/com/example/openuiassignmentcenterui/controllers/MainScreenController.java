package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainScreenController {

    @FXML
    void professorButtonPressed(ActionEvent event){
        try {
            SceneController.switchToScene(event, "professor_sign_in.fxml");
        }
        catch (IOException e){
            Error.ioError();
        }
    }

    @FXML
    void studentButtonPressed(ActionEvent event) {
        try {
            SceneController.switchToScene(event,"student_sign_in.fxml");
        } catch (IOException e) {
            Error.ioError();
        }
    }

}
