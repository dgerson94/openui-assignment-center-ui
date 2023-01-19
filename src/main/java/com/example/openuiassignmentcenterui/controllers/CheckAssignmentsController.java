package com.example.openuiassignmentcenterui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CheckAssignmentsController {

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

    public void initialize(){
        /*
        * TODO: These lists shouldn't be hard coded, they should be gotten from database.
        *  Also the listViews of student and assignment will be bound to course. Depending which course is pick we will
        *  get the relavant assignments and students
         */
        ObservableList<String> courses = FXCollections.observableArrayList("Intro to CS", "Advanced Java", "Data Structures");
        ObservableList<String> assignments = FXCollections.observableArrayList("A1", "A2", "A3","A4");
        ObservableList<String> students = FXCollections.observableArrayList("Dov","Matan","Bob", "John");
        courseListView.setItems(courses);
        assignmentListView.setItems(assignments);
        studentListView.setItems(students);
    }

}
