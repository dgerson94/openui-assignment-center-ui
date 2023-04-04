package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class StudentSignInController {

    @FXML
    private TextField full_name;

    @FXML
    private PasswordField passwordField;

    @FXML
    void logonButtonPressed(ActionEvent event) {
        if (full_name.getText().matches("")){
            Error e = new Error();
            e.raiseError();
        } else if (passwordField.getText().matches("")) {
            Error e = new Error("No password", "You didn't put in any password, please enter a password.");
            e.raiseError();
        }
        else {
            //TODO: Used a professor, need to create a user model and use it for sign in information for both, will make less code.
            String user_name = full_name.getText();
            String password = passwordField.getText();
            StringBuffer response = Https.httpGet(user_name, password, Controller.STUDENT, "http://localhost:8080/courses");
            if (!response.toString().startsWith("Error")) {
                try {
                    Professor user = Controller.createUser(user_name, password);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("student_dashboard.fxml"));
                    Parent root = loader.load();
                    StudentDashboardController sdc = loader.getController();
                    sdc.setUser(user);
                    SceneController.switchToScene(event, root);
                } catch (IOException e) {
                    Error.ioError();
                }
            }
        }
    }

    public void backButtonPressed(ActionEvent event) {
        try {
            SceneController.switchToScene(event,"main_screen.fxml");
        } catch (IOException e) {
            Error.ioError();
        }
    }
}
