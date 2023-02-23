package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.models.Professor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ProfessorDashboardController {

    private Professor user;

    @FXML
    void checkAssignmentsButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("check_assignments.fxml"));
        Parent root = loader.load();
        CheckAssignmentsController cac = loader.getController();
        cac.setProfessor(user);
        SceneController.switchToScene(event, root);
    }

    @FXML
    void setCourseRequirementsButtonPressed(ActionEvent event) throws IOException {
        System.out.println(user.getId() + "    "  + user.getPassword());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_course_requirements.fxml"));
        Parent root = loader.load();
        SetCourseRequirementsController scrc = loader.getController();
        scrc.setProfessor(user);
        SceneController.switchToScene(event, root);
    }

    @FXML
    void logoutButtonPressed(ActionEvent event) throws IOException {
//        TODO: Need to add sign out logic for user with database. For now will just make it a back button.
        SceneController.switchToScene(event,"main_screen.fxml");
    }

    public Professor getUser() {
        return user;
    }

    public void setUser(Professor user) {
        this.user = user;
    }
}
