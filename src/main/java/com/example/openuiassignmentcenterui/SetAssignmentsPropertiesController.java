package com.example.openuiassignmentcenterui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

public class SetAssignmentsPropertiesController {

    @FXML
    private Label Title;

    @FXML
    private DatePicker assignmentDueDatePicker;

    @FXML
    private TextField assignmentFileTextField;

    @FXML
    private ListView<String> assignmentList;

    @FXML
    private DatePicker gradeDueDatePicker;

    @FXML
    private Slider percentageOfCourseGradeSlider;

    public void initialize(){
        ObservableList<String> assignments = createObservableList();
        assignmentList.setItems(assignments);
    }

    @FXML
    void uploadFileButtonPressed(ActionEvent event) {
        /* TODO: Save the file in the database, uplaod textField to show name of file.
         */
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("set_course_requirements.fxml"));
        SceneController scene_switch = new SceneController();
        scene_switch.switchToScene(event,root);
    }

    @FXML
    void saveButtonPressed(ActionEvent event) {
        //TODO: Save the info to database and then return to "set_course_requirements.fxml"
    }

    private ObservableList<String> createObservableList(){
        ObservableList<String> oList = FXCollections.observableArrayList();
        int count = 1;
        for (int i = 0; i < SetCourseRequirementsController.numberOfAssignments; i++){
            oList.add("Assignment " + count);
            count++;
        }
        return oList;
    }

}
