package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
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
            Https.httpPutFile(user.getId(), user.getPassword(), Controller.STUDENT, target, file);
        }
    }

    @FXML
    void viewFeedbackFilePressed(ActionEvent event) throws IOException {
        File tmp;
        String target = Controller.URL_COURSES + "/" + courseId + "/tasks/" + taskId + "/mysubmission/feedbackFile";
        tmp = Https.httpGetFile(user.getId(),user.getPassword(),Controller.STUDENT,target);
        TextArea fileText = new TextArea();
        fileText.setWrapText(true);
        fileText.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");
        fileText.setText(Controller.readText(tmp));
        fileText.setDisable(true);
        VBox root = new VBox(10,fileText);
        root.setAlignment(Pos.CENTER);

        // create a new scene with the VBox as its root
        Scene scene = new Scene(root, 300, 100);

        // create a new stage to display the scene
        Stage popup = new Stage();
        popup.setTitle(tmp.getName());
        popup.setScene(scene);

        // show the popup screen
        popup.show();
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
