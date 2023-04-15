package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Course;
import com.example.openuiassignmentcenterui.models.User;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.openuiassignmentcenterui.helpers.Controller.URL_COURSES;

public class StudentDashboardController {
    private User user;
    private String courseId;
    private String taskId;
    private ArrayList<Course> studentCourses;
    private ArrayList<Task> tasks;
    @FXML
    private ListView<String> coursesViewList;

    @FXML
    private ListView<String> tasksViewList;

    @FXML
    void continueButtonPressed(ActionEvent event) throws IOException {
        if (!tasksViewList.isDisabled()) {
            //if enabled breaks in to two cases:
            String pickedTask = tasksViewList.getSelectionModel().getSelectedItem();
            if (pickedTask == null) {
                Error e = new Error("No task selected", "You did not select a task, please select a course.");
                e.raiseError();
            } else {
                taskId = Controller.getTaskId(pickedTask, tasks);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("student_task_dashboard.fxml"));
                Parent root = loader.load();
                StudentTaskDashboardController stdc = loader.getController();
                stdc.setController(user, taskId, courseId);
                SceneController.switchToScene(event, root);
            }
        } else {
            //If we have reached here then courseView is enabled.
            String pickedCourse = coursesViewList.getSelectionModel().getSelectedItem();
            if (pickedCourse == null) {
                Error e = new Error("No course selected", "You did not select a course, please select a course.");
                e.raiseError();
            } else {
                courseId = Controller.getCourseId(pickedCourse, studentCourses);
                StringBuffer responseTasks = Https.httpGet(user.getId(), user.getPassword(), Controller.STUDENT, Controller.URL_COURSES + "/" + courseId + "/tasks");
                if (responseTasks.toString().equals("[]")) {
                    Error e = new Error("No tasks", "The Professor still hasn't uploaded any tasks, check again at a later date.");
                    e.raiseError();
                } else if (!responseTasks.toString().startsWith("Error")) {
                    TypeToken<ArrayList<Task>> courseType = new TypeToken<>() {
                    };
                    tasks = new Gson().fromJson(String.valueOf(responseTasks), courseType);
                    Controller.updateListsForward(tasksViewList, coursesViewList, Controller.createObservableList(tasks.size()));
                }
            }
        }
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        if (!tasksViewList.isDisabled()) {
            Controller.updateListsBackwards(coursesViewList, tasksViewList);
        } else {
            SceneController.switchToScene(event, "student_sign_in.fxml");
        }
    }


    public void setUser(User user) {
        this.user = user;
        StringBuffer response = Https.httpGet(user.getId(), user.getPassword(), Controller.STUDENT, URL_COURSES);
        if (!response.toString().startsWith("Error")) {
            TypeToken<ArrayList<Course>> courseType = new TypeToken<>() {
            };
            studentCourses = new Gson().fromJson(String.valueOf(response), courseType);
            ArrayList<String> nameOfCourses = Controller.getNameOfCourses(studentCourses);

            ObservableList<String> courses = FXCollections.observableArrayList(nameOfCourses);
            coursesViewList.setItems(courses);
        }
    }
}
