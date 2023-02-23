package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.models.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CheckAssignmentsController {

    private Professor user;
    @FXML
    private ListView<String> assignmentListView;

    @FXML
    private ListView<String> studentListView;

    @FXML
    private ListView<String> courseListView;

    @FXML
    void continueButtonPressed(ActionEvent event) {
        /*
        * TODO: check that all three lists have one item selected, than go to next
         */
    }

    public void initialize() throws IOException {

        ObservableList<String> courses = FXCollections.observableArrayList("Intro to CS", "Advanced Java", "Data Structures");
        ObservableList<String> assignments = FXCollections.observableArrayList("A1", "A2", "A3","A4");
        ObservableList<String> students = FXCollections.observableArrayList("Dov","Matan","Bob", "John");
        courseListView.setItems(courses);
        assignmentListView.setItems(assignments);
        studentListView.setItems(students);
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        SceneController.switchToScene(event, "professor_dashboard.fxml");
    }

    public void setProfessor(Professor professor){
        setUser(professor);
    }

    public Professor getUser() {
        return user;
    }

    public void setUser(Professor user) {
        this.user = user;
    }

}
