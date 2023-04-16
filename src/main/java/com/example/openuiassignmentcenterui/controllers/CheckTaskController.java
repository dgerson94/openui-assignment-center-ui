package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.User;
import com.example.openuiassignmentcenterui.models.Submission;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class CheckTaskController {

    private static final int CREATED = 1;
    private static final String TASKS = "/tasks/";
    private static final String SUBMISSIONS = "/submissions/";
    private User user;
    private String studentId;
    private String taskId;
    private String courseId;
    @FXML
    private TextField gradeTextField;
    @FXML
    private Button sendFeedbackFileButton;
    private boolean gradeChange;
    private boolean noGrade;

    @FXML
    void backButtonPressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("check_tasks.fxml"));
            Parent root = loader.load();
            CheckTasksController ctc = loader.getController();
            int success = ctc.setProfessor(user);
            if (success == CREATED) {
                SceneController.switchToScene(event, root);
            }
        } catch (IOException e) {
            Error.ioError();
        }
    }

    @FXML
    void editButtonPressed(ActionEvent event) {
        gradeTextField.setDisable(false);
        sendFeedbackFileButton.setDisable(false);
        gradeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            sendFeedbackFileButton.setDisable(true);
            gradeChange = true;
        });
    }

    @FXML
    void saveButtonPressed(ActionEvent event) {
        try {
            updateGrade();
            disable();
        } catch (IOException e) {
            Error.ioError();
        }
    }

    private void updateGrade() throws IOException {
        if (gradeChange) {
            String target = Controller.URL_COURSES + "/" + courseId + TASKS + taskId + SUBMISSIONS + studentId + "/grade";
            String grade = gradeTextField.getText();
            if (noGrade) {
                Https.sendJson(user.getId(), user.getPassword(), "POST", Controller.PROFESSOR, target, grade);
            } else {
                Https.sendJson(user.getId(), user.getPassword(), "PUT", Controller.PROFESSOR, target, grade);
            }
        }
    }

    @FXML
    void sendFeedbackFileButtonPressed(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Task File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String target = Controller.URL_COURSES + "/" + courseId + TASKS + taskId + SUBMISSIONS + studentId + "/feedbackFile";
            try {
                boolean hasFile;
                File response = Https.httpGetFile(user.getId(), user.getPassword(), Controller.PROFESSOR, target);
                hasFile = response != null;
                if (response != null)
                    Files.delete(response.toPath());
                Https.httpUploadFile(user.getId(), user.getPassword(), Controller.PROFESSOR, target, file, hasFile);
            } catch (IOException e) {
                Error.ioError();
            }
        }
        disable();
    }

    @FXML
    void downloadSubmissionButtonPressed(ActionEvent event) {
        try {
            String target = Controller.URL_COURSES + "/" + courseId + TASKS + taskId + SUBMISSIONS + studentId + "/file";
            Https.httpGetFile(user.getId(), user.getPassword(), Controller.PROFESSOR, target);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Successful");
            alert.setHeaderText("The Student's answer downloaded successfully.");
            alert.setContentText("You can find the downloaded file in the projects file.");
            alert.showAndWait();
        } catch (Exception e) {
            Error.ioError();
        }

    }

    public void setController(User user, String studentId, String taskId, String courseId) {
        this.user = user;
        this.studentId = studentId;
        this.taskId = taskId;
        this.courseId = courseId;
        String target = Controller.URL_COURSES + "/" + courseId + TASKS + taskId + "/submissions";
        setGrade(target);
    }

    private void setGrade(String target) {
        StringBuffer response = Https.httpGet(user.getId(), user.getPassword(), Controller.PROFESSOR, target);
        if (!response.toString().startsWith("Error")) {
            TypeToken<ArrayList<Submission>> submissionType = new TypeToken<>() {
            };
            ArrayList<Submission> submissions = new Gson().fromJson(String.valueOf(response), submissionType);
            //for now assume there is only one answer, maybe in future make a decision of sorts. Need to fix always true.
            Integer grade = submissions.get(0).getGrade();
            if (grade != null) {
                noGrade = false;
                gradeTextField.setText(grade.toString());
            } else {
                noGrade = true;
            }
        }
    }

    private void disable() {
        gradeTextField.setDisable(true);
        sendFeedbackFileButton.setDisable(true);
    }
}
