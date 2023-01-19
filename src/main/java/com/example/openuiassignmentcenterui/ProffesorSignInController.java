package com.example.openuiassignmentcenterui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ProffesorSignInController {

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
            Error e = new Error("No password", "You didn't put in any passowrd, please enter a password.");
            e.raiseError();
        }
        else {
            String user_name = full_name.getText();
            String password = passwordField.getText();
            /*TODO: check from database the value of password for this user, if correct - sign user into proffesor dashboard
             * else return pop up error that this is the wrong code and try again.
             */
            Boolean succesful_logon = true;
            if (succesful_logon) {
                Parent root = FXMLLoader.load(getClass().getResource("proffesor_dashboard.fxml"));
                SceneController scene_switch = new SceneController();
                scene_switch.switchToScene(event, root);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Incorrect Password");
                alert.setContentText("This password is incorrect, please check the name and password.");
                alert.showAndWait();
            }
        }
    }

}