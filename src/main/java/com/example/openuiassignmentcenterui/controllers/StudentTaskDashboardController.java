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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StudentTaskDashboardController {
    private Professor user;
    private String courseId;
    private String taskId;

    @FXML
    private TextField gradeTextField;
    @FXML
    void uploadFileButtonPressed(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Answer File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String target = Controller.URL_COURSES + "/" + courseId + "/tasks/" + taskId + "/mysubmission/file";
            try {
                boolean hasFile;
                File response = Https.httpGetFile(user.getId(), user.getPassword(),Controller.STUDENT,target);
                if (response == null){
                    hasFile = false;
                } else {
                    hasFile = true;
                }
                Https.httpUploadFile(user.getId(), user.getPassword(), Controller.STUDENT, target, file,hasFile);
            } catch (IOException e) {
                Error.ioError();
            }
        }
    }

    @FXML
    void viewFeedbackFilePressed(ActionEvent event){
        try {
            String target = Controller.URL_COURSES + "/" + courseId + "/tasks/" + taskId + "/mysubmission/feedbackFile";
            Https.httpGetFile(user.getId(),user.getPassword(),Controller.STUDENT,target);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Successful");
            alert.setHeaderText("The Professor's feedback downloaded successfully.");
            alert.setContentText("You can find the downloaded file in the projects file.");
            alert.showAndWait();
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("student_dashboard.fxml"));
        Parent root = loader.load();
        StudentDashboardController sdc = loader.getController();
        sdc.setUser(user);
        SceneController.switchToScene(event, root);
    }

    public void setController(Professor user, String taskId, String courseId) throws IOException {
        this.user = user;
        this.taskId = taskId;
        this.courseId = courseId;
        String target = Controller.URL_COURSES + "/" + courseId + "/tasks/" + taskId +"/mysubmission";
        StringBuffer response = Https.httpGet(user.getId(), user.getPassword(), Controller.STUDENT,target);
        if (response != null) {
            Submission submission = new Gson().fromJson(String.valueOf(response),Submission.class);
            //for now assume there is only one answer, maybe in future make a decision of sorts. Need to fix always error.
            if (submission.getGrade() != null){
                gradeTextField.setText(submission.getGrade().toString());
            }
        }
    }
}
