package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Course;
import com.example.openuiassignmentcenterui.models.Professor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class ProfessorSignInController {

    @FXML
    private TextField full_name;

    @FXML
    private PasswordField passwordField;

    @FXML
    void logonButtonPressed(ActionEvent event) throws IOException {
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
            StringBuffer response = Https.httpGet(user_name,password,Https.PROFESSOR,"http://localhost:8080/courses");
            if (response != null) {
                Professor user = createUser(user_name, password);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("professor_dashboard.fxml"));
                Parent root = loader.load();
                ProfessorDashboardController pdc = loader.getController();
                pdc.setUser(user);
                SceneController.switchToScene(event, root);
            }
        }
    }

    @FXML
    void backButtonPressed (ActionEvent event) throws IOException {
        SceneController.switchToScene(event,"main_screen.fxml");
    }


    private Professor createUser (String user_name, String password){
        Professor professor = new Professor(user_name,password);
        return professor;
    }


}
