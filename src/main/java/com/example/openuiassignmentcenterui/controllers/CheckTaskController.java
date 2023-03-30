package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Submission;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CheckTaskController {

    private Professor user;
    private String studentId;
    private String taskId;
    private String courseId;
    @FXML
    private TextField gradeTextField;

    @FXML
    private Button sendFeedbackFileButton;

    private boolean gradeChange;

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("check_tasks.fxml"));
        Parent root = loader.load();
        CheckTasksController cac = loader.getController();
        cac.setProfessor(user);
        SceneController.switchToScene(event, root);
    }

    @FXML
    void editButtonPressed(ActionEvent event) {
        gradeTextField.setDisable(false);
        sendFeedbackFileButton.setDisable(false);
        gradeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            gradeChange = true;
        });
    }

    @FXML
    void saveButtonPressed(ActionEvent event) throws IOException {
        updateGrade();
        disable();
    }

    private void updateGrade() throws IOException {
        if (gradeChange){
            String target = Controller.URL_COURSES + "/" + courseId + "/tasks/" + taskId +"/submissions/" + studentId +"/grade";
            String grade = gradeTextField.getText();
            Https.sendJson(user.getId(),user.getPassword(),"POST",Controller.PROFESSOR, target, grade);
        }
    }

    @FXML
    void sendFeedbackFileButtonPressed(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Task File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String target = Controller.URL_COURSES + "/" + courseId + "/tasks/" + taskId + "/submissions/" + studentId + "/feedbackFile";
            Https.httpPutFile(user.getId(), user.getPassword(), Controller.PROFESSOR, target, file);
        }
        disable();
    }

    @FXML
    void downloadSubmissionButtonPressed(ActionEvent event) {
        try {
            String target = Controller.URL_COURSES + "/" + courseId + "/tasks/" + taskId + "/submissions/" + studentId + "/file";
            Https.httpGetFile(user.getId(),user.getPassword(),Controller.PROFESSOR,target);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Successful");
            alert.setHeaderText("The Student's answer downloaded successfully.");
            alert.setContentText("You can find the downloaded file in the projects file.");
            alert.showAndWait();
        } catch (Exception e) {
            Error.ioError();
        }

    }

    public void setController(Professor user, String studentId, String taskId, String courseId) throws IOException {
        this.user = user;
        this.studentId = studentId;
        this.taskId = taskId;
        this.courseId = courseId;
        String target = Controller.URL_COURSES + "/" + courseId + "/tasks/" + taskId +"/submissions";
        setGrade(target);
    }

    private void setGrade(String target) throws IOException {
        StringBuffer response = Https.httpGet(user.getId(),user.getPassword(),Controller.PROFESSOR, target);
        if (!response.toString().equals("[]")) {
            TypeToken<ArrayList<Submission>> submissionType = new TypeToken<>() {
            };
            ArrayList<Submission> submissions = new Gson().fromJson(String.valueOf(response), submissionType);
            //for now assume there is only one answer, maybe in future make a decision of sorts. Need to fix always true.
            String grade = submissions.get(0).getGrade().toString();
            if (grade != null) {
                gradeTextField.setText(grade);
            }
        } else {
            //Think of error, there should always be a response here. Server Error.
        }
    }

    private void disable(){
        gradeTextField.setDisable(true);
        sendFeedbackFileButton.setDisable(true);
    }
}
