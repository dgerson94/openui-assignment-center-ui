package com.example.openuiassignmentcenterui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

import java.io.IOException;



public class SetCourseRequirementsController {

    static int numberOfAssignments = 0;
    static String course = "";

    @FXML
    private ListView<String> listOfCourses;

    @FXML
    private ChoiceBox<Integer> numberOfAssignmentsPicker;

    public void initialize (){
        ObservableList<String> courses = FXCollections.observableArrayList("Intro to CS", "Advanced Java", "Data Structures");
        ObservableList<Integer> numberOfCourses = FXCollections.observableArrayList(3,4,5,6);

        listOfCourses.setItems(courses);
        numberOfAssignmentsPicker.setItems(numberOfCourses);
    }

    @FXML
    void continueButtonPressed(ActionEvent event) throws IOException {
        if (listOfCourses.getSelectionModel().getSelectedItem() == null) {
            Error e = new Error("No course selected","You did not select a course, please select a course.");
            e.raiseError();

        } else if (numberOfAssignmentsPicker.getValue() == null) {
            Error e = new Error("No number selected.","You did not select how many assignments will be in the course. Please select how many there will be.");
            e.raiseError();
        }
        numberOfAssignments = numberOfAssignmentsPicker.getValue();
        course = listOfCourses.getSelectionModel().getSelectedItem();
        /*TODO:Need to fix that it saves the info in database, not just pass on to next controller
        *  Also Need to make it that there is no option to change requirments after assignmentDueDate
        */

        Parent root = FXMLLoader.load(getClass().getResource("set_assignments_properties.fxml"));
        SceneController scene_switch = new SceneController();
        scene_switch.switchToScene(event,root);
    }



}
