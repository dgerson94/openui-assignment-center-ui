package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Course;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class StudentDashboardController {
    private Professor user;
    @FXML
    private ListView<String> coursesViewList;

    @FXML
    private ListView<?> tasksViewList;

    @FXML
    void continueButtonPressed(ActionEvent event) {

    }

    @FXML
    void logoutButtonPressed(ActionEvent event) {

    }


    public void setUser(Professor user) throws IOException {
        this.user = user;
        StringBuffer response = Https.httpGet(user.getId(), user.getPassword(), Controller.STUDENT, Controller.URL_COURSES);
        TypeToken<ArrayList<Course>> courseType = new TypeToken<>() {
        };
        ArrayList<Course> studentCourses = new Gson().fromJson(String.valueOf(response), courseType);
        ArrayList<String> nameOfCourses = Controller.getNameOfCourses(studentCourses);

        ObservableList<String> courses = FXCollections.observableArrayList(nameOfCourses);
        coursesViewList.setItems(courses);
    }
}
