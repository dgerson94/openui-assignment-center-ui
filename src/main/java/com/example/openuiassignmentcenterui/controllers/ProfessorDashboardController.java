package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ProfessorDashboardController {

    private static final int CREATED = 1;
    private User user;

    @FXML
    void checkTasksButtonPressed(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("check_tasks.fxml"));
        Parent root;
        try {
            root = loader.load();
            CheckTasksController ctc = loader.getController();
            int success = ctc.setProfessor(user);
            if (success == CREATED) {
                SceneController.switchToScene(event, root);
            }
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
            scrc.setUser(user);
            SceneController.switchToScene(event, root);
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {
        try {
            SceneController.switchToScene(event, "main_screen.fxml");
        } catch (IOException e) {
            Error.ioError();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
