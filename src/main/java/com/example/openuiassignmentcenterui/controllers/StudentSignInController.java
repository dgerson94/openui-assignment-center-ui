package com.example.openuiassignmentcenterui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    void logonButtonPressed(ActionEvent event) throws IOException {
        String user_name = full_name.getText();
        String password = passwordField.getText();
////       Note that I have changed the student on server to get the answer I want, will need to make sure Matan changes
//        also. Also need to deal with the answer in a different way.
//        Boolean succesful_logon = check_for_students(user_name,password);
        Boolean succesful_logon = true;
        if (succesful_logon){
            SceneController.switchToScene(event,"student_dashboard.fxml");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect Password");
            alert.setContentText("This password is incorrect, please check the name and password.");
            alert.showAndWait();
        }
    }

    private Boolean check_for_students(String name,String password) throws IOException {
        URL url = new URL("http://localhost:8080/students?id=1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // optional default is GET
        conn.setRequestProperty("Content-Type", "application/json");

        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        String answer = (response.toString());
        System.out.println(answer);
        if (answer.contains(name) && answer.contains(password))
            return true;
        return false;
    }

}
