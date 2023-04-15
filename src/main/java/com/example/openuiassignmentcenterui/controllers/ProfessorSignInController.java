package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class ProfessorSignInController {

    @FXML
    private TextField fullName;

    @FXML
    private PasswordField passwordField;

    @FXML
    void logonButtonPressed(ActionEvent event) {
        try {
            if (fullName.getText().matches("")){
                Error e = new Error();
                e.raiseError();
            } else if (passwordField.getText().matches("")) {
                Error e = new Error("No password", "You didn't put in any password, please enter a password.");
                e.raiseError();
            }
            else {
                String userName = fullName.getText();
                String password = passwordField.getText();
                StringBuffer response = Https.httpGet(userName,password, Controller.PROFESSOR,"http://localhost:8080/courses");
                if (!response.toString().startsWith("Error")) {
                    Professor user = createUser(userName, password);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("professor_dashboard.fxml"));
                    Parent root = loader.load();
                    ProfessorDashboardController pdc = loader.getController();
                    pdc.setUser(user);
                    SceneController.switchToScene(event, root);
                }
            }
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void backButtonPressed (ActionEvent event) {
        try {
            SceneController.switchToScene(event,"main_screen.fxml");
        } catch (IOException e) {
            Error.ioError();
        }
    }


    private Professor createUser (String userName, String password){
        return new Professor(userName,password);
    }


}
