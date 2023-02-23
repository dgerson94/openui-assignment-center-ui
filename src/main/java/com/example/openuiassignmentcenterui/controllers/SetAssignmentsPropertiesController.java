package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.models.Task;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;

public class SetAssignmentsPropertiesController {

    private ArrayList<Task> tasks;

    private Integer courseId;
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


    @FXML
    void uploadFileButtonPressed(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Assignment File");
        File file = fileChooser.showOpenDialog(stage);
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        SceneController.switchToScene(event,"set_course_requirements.fxml");
    }

    @FXML
    void editButtonPressed(ActionEvent event) {
        //TODO: Enable changing the info of a certain assignment.

    }

    @FXML
    void saveButtonPressed(ActionEvent event) {
        //TODO: Save the info to database and then return to "set_course_requirements.fxml"
    }

    private ObservableList<String> createObservableList(Integer size){
        ObservableList<String> oList = FXCollections.observableArrayList();
        int count = 1;
        for (int i = 0; i < size; i++){
            oList.add("Assignment " + count);
            count++;
        }
        return oList;
    }

    public void setTasks(ArrayList<Task> tasks, Integer valueOf) {
        this.tasks = tasks;
        this.courseId = valueOf;
        ObservableList<String> assignments = createObservableList(tasks.size());
        assignmentList.setItems(assignments);
        onItemSelected(assignmentList);

    }



    private LocalDate makeLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault(); // Or you can specify a specific time zone
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        LocalDate localDate = zonedDateTime.toLocalDate();
        return localDate;
    }

    private Task getCurrentTask(String currentTaskString) {
        Task task = null;
        for (int i = 0; i < tasks.size(); i++){
            if (("Assignment "+ tasks.get(i).getId()).matches(currentTaskString)) {
                task = tasks.get(i);
            }
        }
        return task;
    }

    private void onItemSelected(ListView<String> listView) {
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String oldVal, String newVal) -> {
                    if (newVal != null) {
                        String currentTaskString = assignmentList.getSelectionModel().getSelectedItem();
                        Task currentTask = getCurrentTask(currentTaskString);
                        percentageOfCourseGradeSlider.adjustValue(currentTask.getWeightInGrade() * 100);
                        gradeDueDatePicker.setValue(makeLocalDate(currentTask.getCheckDeadLine()));
                        assignmentDueDatePicker.setValue(makeLocalDate(currentTask.getSubmissionDeadline()));
                    }
                });
    }

}
