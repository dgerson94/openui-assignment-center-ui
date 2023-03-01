package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    private Professor user;

    private Integer courseId;
    @FXML
    private Label Title;

    private Task currentTask;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_course_requirements.fxml"));
        Parent root = loader.load();
        SetCourseRequirementsController scrc = loader.getController();
        scrc.setProfessor(user);
        SceneController.switchToScene(event, root);
    }

    @FXML
    void editButtonPressed(ActionEvent event) {
        String pickedTaskName = assignmentList.getSelectionModel().getSelectedItem();
        Task pickedTask = getTask(pickedTaskName.substring(pickedTaskName.length()-1));
        setDisable(percentageOfCourseGradeSlider,gradeDueDatePicker,assignmentDueDatePicker,false);
    }

    private Task getTask(String pickedTaskName) {
        Task ret = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().matches(pickedTaskName))
                ret = tasks.get(i);
        }
        return ret;
    }

    @FXML
    void saveButtonPressed(ActionEvent event) throws IOException {
        //TODO: Need to have two different actions - One for saving all info in Json and one for sending File.
        currentTask.setCheckDeadLine(makeDate(gradeDueDatePicker.getValue()));
        currentTask.setSubmissionDeadline(makeDate(assignmentDueDatePicker.getValue()));
        currentTask.setWeightInGrade(percentageOfCourseGradeSlider.getValue());
        sendJson();
        sendFile();
        setDisable(percentageOfCourseGradeSlider,gradeDueDatePicker,assignmentDueDatePicker,true);
    }

    private void sendFile() {

    }

    private void sendJson() throws IOException {
        Task tmp = new Task(currentTask.getId(),currentTask.getSubmissionDeadline(),currentTask.getCheckDeadLine(),currentTask.getWeightInGrade());
        //tmp.setCourseId(courseId);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(tmp);
        StringBuffer response = Https.httpPost(user.getId(),user.getPassword(),null,"http://localhost:8080/courses/1/tasks",jsonResponse);
        System.out.println(response.toString());
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

    public void setTasks(Professor user, ArrayList<Task> tasks, Integer courseId) {
        this.user = user;
        this.tasks = tasks;
        this.courseId = courseId;
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

    private Date makeDate (LocalDate localDate){
        LocalDateTime localDateTime = localDate.atStartOfDay(); // get the start of the day
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault()); // get the ZonedDateTime
        Instant instant = zonedDateTime.toInstant(); // get the Instant
        Date date = Date.from(instant); // convert Instant to Date
        return date;
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
                        currentTask = getCurrentTask(currentTaskString);
                        percentageOfCourseGradeSlider.adjustValue(currentTask.getWeightInGrade() * 100);
                        gradeDueDatePicker.setValue(makeLocalDate(currentTask.getCheckDeadLine()));
                        assignmentDueDatePicker.setValue(makeLocalDate(currentTask.getSubmissionDeadline()));
                        setDisable(percentageOfCourseGradeSlider,gradeDueDatePicker,assignmentDueDatePicker,true);
                    }
                });
    }

    private void setDisable(Slider s, DatePicker dp1, DatePicker dp2,Boolean b){
        s.setDisable(b);
        dp1.setDisable(b);
        dp2.setDisable(b);
    }

}
