package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Course;
import com.example.openuiassignmentcenterui.models.User;
import com.example.openuiassignmentcenterui.models.Submission;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class CheckTasksController {

    private static final int CREATED = 1;
    private static final int FAILED_TO_CREATE = -1;
    private static final String URL_COURSES = "http://localhost:8080/courses";
    private User user;
    @FXML
    private ListView<String> taskListView;
    @FXML
    private ListView<String> studentListView;
    @FXML
    private ListView<String> courseListView;
    private ArrayList<Course> professorCourses;
    private ArrayList<Task> tasks;
    private String pickedCourse;
    private String pickedTask;
    //pickedStudent is essentially the id, they are both just a number
    private String pickedStudent;

    private String courseId;
    private String taskId;
    @FXML
    private Label studentLabel;
    @FXML
    private Label taskLabel;

    @FXML
    void continueButtonPressed(ActionEvent event){
        // In case that studentListView is enabled we have all the information, go to checkTask screen
        if (!studentListView.isDisabled()) {
            studentContinue(event);
        } else {
            //In Case that taskListView is enabled we want to take the selected task and get relevant students.
            if (!taskListView.isDisabled()) {
                taskContinue();
            } else {
                courseContinue();
            }
        }
    }

    private void courseContinue() {
        //If we have reached here than only the courseListView is enabled, we will get the tasks.
        pickedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (pickedCourse == null) {
            Error e = new Error("No course selected", "You did not select a course, please select a course.");
            e.raiseError();
        } else {
            courseId = Controller.getCourseId(pickedCourse, professorCourses);
            StringBuffer responseTasks = Https.httpGet(user.getId(), user.getPassword(), Controller.PROFESSOR, URL_COURSES + "/" + courseId + "/tasks");
            if (responseTasks.toString().equals("[]")) {
                Error e = new Error("No tasks in this course.", "You have not set any tasks for this course yet. How can your students do work you didn't assign?");
                e.raiseError();
            } else if (!responseTasks.toString().startsWith("Error")) {
                TypeToken<ArrayList<Task>> courseType = new TypeToken<>() {
                };
                tasks = new Gson().fromJson(String.valueOf(responseTasks), courseType);
                Controller.updateListsForward(taskListView, courseListView, Controller.createObservableList(tasks.size()));
            }
        }
    }

    private void taskContinue() {
        pickedTask = taskListView.getSelectionModel().getSelectedItem();
        if (pickedTask == null) {
            Error e = new Error("No task selected", "You did not select a task, please select a course.");
            e.raiseError();
        } else {
            taskId = Controller.getTaskId(pickedTask, tasks);
            StringBuffer responseStudents = Https.httpGet(user.getId(), user.getPassword(), Controller.PROFESSOR, URL_COURSES + "/" + courseId + "/tasks/" + taskId + "/submissions");
            if (responseStudents.toString().equals("[]")) {
                Error e = new Error("No students in this course.", "No students have given in their answers yet.");
                e.raiseError();
            } else if (!responseStudents.toString().startsWith("Error")) {
                TypeToken<ArrayList<Submission>> submissionType = new TypeToken<>() {
                };
                ArrayList<Submission> submissions = new Gson().fromJson(String.valueOf(responseStudents), submissionType);
                Controller.updateListsForward(studentListView, taskListView, Controller.createObservableStudentList(submissions));
            }
        }
    }

    private void studentContinue(ActionEvent event) {
        pickedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (pickedStudent == null){
            Error e = new Error("No students selected.", "You did not a student ID. Please select a student ID to continue.");
            e.raiseError();
        } else {
            //implement opening check task controller with correct info.
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("check_task.fxml"));
                Parent root = loader.load();
                CheckTaskController ctc = loader.getController();
                ctc.setController(user, pickedStudent, taskId, courseId);
                SceneController.switchToScene(event, root);
            } catch (IOException e) {
                Error.ioError();
            }
        }
    }


    @FXML
    void backButtonPressed(ActionEvent event) {
        if (!studentListView.isDisabled()) {
            Controller.updateListsBackwards(taskListView, studentListView);
        } else if (!taskListView.isDisabled()) {
            tasks = null;
            Controller.updateListsBackwards(courseListView, taskListView);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("professor_dashboard.fxml"));
                Parent root = loader.load();
                ProfessorDashboardController pdc = loader.getController();
                pdc.setUser(user);
                SceneController.switchToScene(event, root);
            } catch (IOException e) {
                Error.ioError();
            }
        }
    }

    public int setProfessor(User user) {
        this.user = user;
        professorCourses = Controller.initializeController(this.user, courseListView);
        if (professorCourses.isEmpty()) {
            return FAILED_TO_CREATE;
        } else {
            return CREATED;
        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
