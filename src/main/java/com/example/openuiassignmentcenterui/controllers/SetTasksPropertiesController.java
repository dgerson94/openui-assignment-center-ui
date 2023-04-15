package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.helpers.SceneController;
import com.example.openuiassignmentcenterui.models.User;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SetTasksPropertiesController {

    private static final String URL_COURSES = "http://localhost:8080/courses/";
    private ArrayList<Task> tasks;
    private User user;
    private String fileAbsolutePath;
    private Integer courseId;
    private Task currentTask;
    @FXML
    private DatePicker taskDueDatePicker;
    @FXML
    private TextField taskFileTextField;
    @FXML
    private ListView<String> taskList;
    @FXML
    private DatePicker gradeDueDatePicker;
    @FXML
    private Slider percentageOfCourseGradeSlider;
    @FXML
    private Button uploadFileButton;
    private String urlTask;

    private boolean updatedJson = false;

    private boolean updatedFile = false;


    @FXML
    void uploadFileButtonPressed(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Task File");
        File file = fileChooser.showOpenDialog(stage);
        fileAbsolutePath = file.getAbsolutePath();
        taskFileTextField.setText(file.getName());
        updatedFile = true;
    }

    @FXML
    void backButtonPressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("set_course_requirements.fxml"));
            Parent root = loader.load();
            SetCourseRequirementsController scrc = loader.getController();
            scrc.setUser(user);
            SceneController.switchToScene(event, root);
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void editButtonPressed(ActionEvent event) {
        setDisable(percentageOfCourseGradeSlider, gradeDueDatePicker, taskDueDatePicker, uploadFileButton, false);
    }

    @FXML
    void saveButtonPressed(ActionEvent event) {
        String newCheckDeadLine = String.valueOf(gradeDueDatePicker.getValue());
        String newSubmissionDeadline = String.valueOf(taskDueDatePicker.getValue());
        Double newWeightInGrade = percentageOfCourseGradeSlider.getValue() / 100;
        if (!newWeightInGrade.equals(currentTask.getWeightInGrade())) {
            currentTask.setWeightInGrade(newWeightInGrade);
            updatedJson = true;
        }
        if (!newSubmissionDeadline.equals(currentTask.getSubmissionDeadline().substring(0, 10))) {
            currentTask.setSubmissionDeadline(newSubmissionDeadline);
            updatedJson = true;
        }
        if (!newCheckDeadLine.equals(currentTask.getCheckDeadLine().substring(0, 10))) {
            currentTask.setCheckDeadLine(newCheckDeadLine);
            updatedJson = true;
        }
        if (updatedJson) {
            try {
                sendJson();
            } catch (IOException e) {
                Error.ioError();
            }
            updatedJson = false;
        }
        if (updatedFile) {
            sendFile();
            updatedFile = false;
        }
        setDisable(percentageOfCourseGradeSlider, gradeDueDatePicker, taskDueDatePicker, uploadFileButton, true);
    }

    private void sendFile() {
        File file = new File(fileAbsolutePath);
        Https.httpUploadFile(user.getId(), user.getPassword(), Controller.PROFESSOR, urlTask + "/file", file, currentTask.getHasFile());
    }

    private void sendJson() throws IOException {
        Task tmp = new Task(currentTask.getId(), currentTask.getSubmissionDeadline(), currentTask.getCheckDeadLine(), currentTask.getWeightInGrade());
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(tmp);
        Https.sendJson(user.getId(), user.getPassword(), "PUT", Controller.PROFESSOR, urlTask, jsonResponse);
    }

    public void setTasks(User user, ArrayList<Task> tasks, Integer courseId) {
        this.user = user;
        this.tasks = tasks;
        this.courseId = courseId;
        ObservableList<String> assignments = Controller.createObservableList(tasks.size());
        taskList.setItems(assignments);
        onItemSelected(taskList);
    }


    private LocalDate makeLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); // the format of the input string
        return LocalDate.parse(date, formatter);
    }

    private Task getCurrentTask(String currentTaskString) {
        Task task = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (("Assignment " + tasks.get(i).getId()).matches(currentTaskString)) {
                task = tasks.get(i);
            }
        }
        return task;
    }

    private void onItemSelected(ListView<String> listView) {
        listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String oldVal, String newVal) -> {
            if (newVal != null) {
                String currentTaskString = taskList.getSelectionModel().getSelectedItem();
                currentTask = getCurrentTask(currentTaskString);
                percentageOfCourseGradeSlider.adjustValue(currentTask.getWeightInGrade() * 100);
                gradeDueDatePicker.setValue(makeLocalDate(currentTask.getCheckDeadLine()));
                taskDueDatePicker.setValue(makeLocalDate(currentTask.getSubmissionDeadline()));
                urlTask = URL_COURSES + courseId + "/tasks/" + currentTask.getId();
                try {
                    File file = Https.httpGetFile(user.getId(), user.getPassword(), Controller.PROFESSOR, urlTask + "/file");
                    if (file != null) {
                        taskFileTextField.setText(file.getName());
                        Files.delete(file.toPath());
                    } else {
                        taskFileTextField.clear();
                        taskFileTextField.setPromptText("Please Upload Task");
                    }
                    setDisable(percentageOfCourseGradeSlider, gradeDueDatePicker, taskDueDatePicker, uploadFileButton, true);
                } catch (IOException e) {
                    Error.ioError();
                }
            }
        });
    }

    private void setDisable(Slider s, DatePicker dp1, DatePicker dp2, Button btn, Boolean b) {
        s.setDisable(b);
        dp1.setDisable(b);
        dp2.setDisable(b);
        btn.setDisable(b);
    }
}