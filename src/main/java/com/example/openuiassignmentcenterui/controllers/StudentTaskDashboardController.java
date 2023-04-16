package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.User;
import com.example.openuiassignmentcenterui.models.Submission;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StudentTaskDashboardController {
    private User user;
    private String courseId;
    private String taskId;

    @FXML
    private TextField gradeTextField;

    public static final String TASKS = "/tasks/";

    @FXML
    void uploadFileButtonPressed(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Answer File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String target = Controller.URL_COURSES + "/" + courseId + TASKS + taskId + "/mysubmission/file";
            try {
                boolean hasFile;
                File response = Https.httpGetFile(user.getId(), user.getPassword(), Controller.STUDENT, target);
                hasFile = response != null;
                if (response != null)
                    Files.delete(response.toPath());
                Https.httpUploadFile(user.getId(), user.getPassword(), Controller.STUDENT, target, file, hasFile);
            } catch (IOException e) {
                Error.ioError();
            }
        }
    }

    @FXML
    void downloadFeedbackFilePressed(ActionEvent event) {
        try {
            boolean downloaded = false;
            String target = Controller.URL_COURSES + "/" + courseId + TASKS + taskId + "/mysubmission/feedbackFile";
            File response = Https.httpGetFile(user.getId(), user.getPassword(), Controller.STUDENT, target, true);
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            File save = fileChooser.showSaveDialog(stage);
            if (response != null) {
                downloaded = response.renameTo(save);
            }
            if (downloaded) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Download Successful");
                alert.setHeaderText("The Professor's feedback downloaded successfully.");
                alert.setContentText("You can find the downloaded file in the projects file.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void downloadTaskFilePressed (ActionEvent event){
        try {
            boolean downloaded = false;
            String target = Controller.URL_COURSES + "/" + courseId + TASKS + taskId + "/file";
            File response = Https.httpGetFile(user.getId(), user.getPassword(), Controller.STUDENT, target, true);
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            File save = fileChooser.showSaveDialog(stage);
            if (response != null) {
                downloaded = response.renameTo(save);
            }
            if (downloaded) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Download Successful");
                alert.setHeaderText("The Task file was downloaded successfully.");
                alert.setContentText("You can find the downloaded file in the projects file.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void backButtonPressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_dashboard.fxml"));
            Parent root = loader.load();
            StudentDashboardController sdc = loader.getController();
            sdc.setUser(user);
            SceneController.switchToScene(event, root);
        } catch (IOException e) {
            Error.ioError();
        }
    }

    public void setController(User user, String taskId, String courseId) {
        this.user = user;
        this.taskId = taskId;
        this.courseId = courseId;
        String target = Controller.URL_COURSES + "/" + courseId + TASKS + taskId + "/mysubmission";
        StringBuffer response = Https.httpGet(user.getId(), user.getPassword(), Controller.STUDENT, target);
        if (response.toString().equals("No Submission.")) {
            System.out.println("There was no student submission.");
        } else if (!response.toString().startsWith("Error")) {
            Submission submission = new Gson().fromJson(String.valueOf(response), Submission.class);
            //for now assume there is only one answer, maybe in future make a decision of sorts. Need to fix always error.
            if (submission.getGrade() != null) {
                gradeTextField.setText(submission.getGrade().toString());
            }
        }
    }
}
