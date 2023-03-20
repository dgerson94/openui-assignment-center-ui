package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Course;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Student;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;

public class CheckTasksController {

    private Professor user;
    @FXML
    private ListView<String> taskListView;

    @FXML
    private ListView<String> studentListView;

    @FXML
    private ListView<String> courseListView;
    private ArrayList<Course> professorCourses;

    private String pickedCourse;
    private String pickedTask;
    private String pickedStudent;
    @FXML
    private Label studentLabel;

    @FXML
    private Label taskLabel;

    private final String URL_COURSES = "http://localhost:8080/courses";

    @FXML
        /*TODO: Need to make these things that save per controller and then disable each row after something was selected and continue was picked.
        TODO: Cam go back with a back button. Need to add back button and disable lists after selection.
         */
    void continueButtonPressed(ActionEvent event) throws IOException {
        String pickedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (pickedCourse == null){
            Error e = new Error("No course selected", "You did not select a course, please select a course.");
            e.raiseError();
        } else {
            String courseId = Controller.getCourseId(pickedCourse,professorCourses);
            StringBuffer responseTasks = Https.httpGet(user.getId(), user.getPassword(), Https.PROFESSOR, URL_COURSES + "/" + courseId + "/tasks");
            if (!responseTasks.toString().equals("[]")) {
                TypeToken<ArrayList<Task>> courseType = new TypeToken<>() {
                };
                ArrayList<Task> tasks = new Gson().fromJson(String.valueOf(responseTasks), courseType);
                taskListView = new ListView<>(Controller.createObservableList(tasks.size()));
                String pickedTask = taskListView.getSelectionModel().getSelectedItem();
                if (pickedTask == null){
                    Error e = new Error("No task selected", "You did not select a task, please select a course.");
                    e.raiseError();
                } else{
                    String taskId = Controller.getTaskId(pickedTask,tasks);
                    StringBuffer responseStudents = Https.httpGet(user.getId(),user.getPassword(),Https.PROFESSOR,URL_COURSES + "/" + courseId + "/tasks" + taskId);
                    if (!responseStudents.toString().equals("[]")){
                        TypeToken<ArrayList<Student>> studentType = new TypeToken<>() {
                        };
                        ArrayList<Student> students = new Gson().fromJson(String.valueOf(responseStudents), studentType);
                        studentListView = new ListView<>(Controller.createObservableStudentList(students));
                        String pickedStudent = studentListView.getSelectionModel().getSelectedItem();
                        //flow here not finished, stopped to change flow logic need to add call to get students info and switch controllers.
                    } else {
                        Error e = new Error("No students in this course.", "Call the rector and complain that no one signed up for your course.");
                        e.raiseError();
                    }
                }


            } else {
                //if there is an empty string - error, can't check before setting tasks
                Error e = new Error("No tasks.", "You did not set any requirements for this course. Please first set requirements.");
                e.raiseError();
            }
        }

    }


    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        //TODO: Needs to be updated for flow with disabled lists.
        SceneController.switchToScene(event, "professor_dashboard.fxml");
    }

    public void setProfessor(Professor professor) throws IOException {
        this.user = professor;
        professorCourses = Controller.initializeController(user,courseListView);
    }

    public Professor getUser() {
        return user;
    }

    public void setUser(Professor user) {
        this.user = user;
    }

}
