package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.models.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ProfessorDashboardController {

    private Professor user;

    @FXML
    void checkTasksButtonPressed(ActionEvent event)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("check_tasks.fxml"));
        Parent root;
        try {
            root = loader.load();
            CheckTasksController cac = loader.getController();
            cac.setProfessor(user);
            SceneController.switchToScene(event, root);
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void setCourseRequirementsButtonPressed(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_course_requirements.fxml"));
        Parent root;
        try {
            root = loader.load();
            SetCourseRequirementsController scrc = loader.getController();
            scrc.setProfessor(user);
            SceneController.switchToScene(event, root);
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void logoutButtonPressed(ActionEvent event){
        try {
            SceneController.switchToScene(event,"main_screen.fxml");
        } catch (IOException e) {
            Error.ioError();
        }
    }

    public Professor getUser() {
        return user;
    }

    public void setUser(Professor user) {
        this.user = user;
    }
}
