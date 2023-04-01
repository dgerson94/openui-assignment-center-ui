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
    private TextField full_name;

    @FXML
    private PasswordField passwordField;

    @FXML
    void logonButtonPressed(ActionEvent event) {
        try {
            if (full_name.getText().matches("")){
                Error e = new Error();
                e.raiseError();
            } else if (passwordField.getText().matches("")) {
                Error e = new Error("No password", "You didn't put in any password, please enter a password.");
                e.raiseError();
            }
            else {
                String user_name = full_name.getText();
                String password = passwordField.getText();
                StringBuffer response = Https.httpGet(user_name,password, Controller.PROFESSOR,"http://localhost:8080/courses");
                if (!response.toString().equals("Error")) {
                    Professor user = createUser(user_name, password);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("professor_dashboard.fxml"));
                    Parent root = loader.load();
                    ProfessorDashboardController pdc = loader.getController();
                    pdc.setUser(user);
                    SceneController.switchToScene(event, root);
                }
            }
        } catch (IOException e) {
            Error.ioError();
        } //TODO: also need to catch and deal with HTTPS errors here.
    }

    @FXML
    void backButtonPressed (ActionEvent event) {
        try {
            SceneController.switchToScene(event,"main_screen.fxml");
        } catch (IOException e) {
            Error.ioError();
        }
    }


    private Professor createUser (String user_name, String password){
        Professor professor = new Professor(user_name,password);
        return professor;
    }


}
