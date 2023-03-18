package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Course;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;

public class CheckAssignmentsController {

    private Professor user;
    @FXML
    private ListView<String> assignmentListView;

    @FXML
    private ListView<String> studentListView;

    @FXML
    private ListView<String> courseListView;
    private ArrayList<Course> professorCourses;
    @FXML
    private Label studentLabel;

    @FXML
    private Label taskLabel;

    private final String URL_COURSES = "http://localhost:8080/courses";

    @FXML
    void continueButtonPressed(ActionEvent event) throws IOException {
       String pickedCourse = courseListView.getSelectionModel().getSelectedItem();
       if (pickedCourse == null){
           Error e = new Error("No course selected", "You did not select a course, please select a course.");
           e.raiseError();
       } else {
            String courseId = Controller.getCourseId(pickedCourse,professorCourses);
            StringBuffer response = Https.httpGet(user.getId(), user.getPassword(), Https.PROFESSOR, URL_COURSES + "/" + courseId + "/tasks");
           if (!response.toString().equals("[]")) {
               TypeToken<ArrayList<Task>> courseType = new TypeToken<>() {
               };
               //make into a private variable? -> need to think this flow through.
               ArrayList<Task> tasks = new Gson().fromJson(String.valueOf(response), courseType);
               //observable

           } else {
               //if there is an empty string - error, can't check before setting tasks
               Error e = new Error("No tasks.", "You did not set any requirements for this course. Please first set requirements.");
               e.raiseError();
           }
       }

    }


    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        SceneController.switchToScene(event, "professor_dashboard.fxml");
    }

    public void setProfessor(Professor professor) throws IOException {
        //TODO: Flow setting this up is very similar to course requirements - make usecase for both
        this.user = professor;
        professorCourses = Controller.initializeController(user,courseListView);
    }

    public Professor getUser() {
        return user;
    }

    public void setUser(Professor user) {
        this.user = user;
    }

}
