package com.example.openuiassignmentcenterui.controllers;

import com.example.openuiassignmentcenterui.helpers.Controller;
import com.example.openuiassignmentcenterui.helpers.Error;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Course;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;


public class SetCourseRequirementsController {
    private static final String DEFAULT_DATE = "2023-01-01T00:00:00.000+00:00";

    private static final double DEFAULT_PERCENTAGE = 0.1;
    private static final int CREATED = 1;
    private static final int FAILED_TO_CREATE = -1;
    private Professor user;
    private ArrayList<Course> professorCourses;
    @FXML
    private ListView<String> listOfCourses;
    @FXML
    private ChoiceBox<Integer> numberOfTasksPicker;
    @FXML
    private Label numberOfTasksWarning;
    @FXML
    private Label numberOfTasksLabel;

    private final String URL_COURSES = "http://localhost:8080/courses";


    @FXML
    void continueButtonPressed(ActionEvent event) throws IOException {
        String pickedCourse = listOfCourses.getSelectionModel().getSelectedItem();
        if (pickedCourse == null) {
            Error e = new Error("No course selected", "You did not select a course, please select a course.");
            e.raiseError();
        } else {
            String courseId = Controller.getCourseId(pickedCourse,professorCourses);
            StringBuffer response = Https.httpGet(user.getId(), user.getPassword(), Controller.PROFESSOR, URL_COURSES + "/" + courseId + "/tasks");
            if (!response.toString().equals("[]")) {
                TypeToken<ArrayList<Task>> courseType = new TypeToken<>() {
                };
                ArrayList<Task> tasks = new Gson().fromJson(String.valueOf(response), courseType);
                goToSetTasks(tasks, courseId, event);
            } else {
                if (numberOfTasksPicker.isVisible()) {
                    if (numberOfTasksPicker.getValue() == null) {
                        Error e = new Error("No number selected.", "You did not select how many tasks will be in the course. Please select how many there will be.");
                        e.raiseError();
                    } else {
                        Integer numberOfTasks = numberOfTasksPicker.getValue();
                        ArrayList<Task> tasks = makeEmptyTasks(numberOfTasks);
                        postTasks(tasks,courseId);
                        goToSetTasks(tasks, courseId, event);
                    }
                } else {
                    numberOfTasksPicker.setVisible(true);
                    numberOfTasksLabel.setVisible(true);
                    numberOfTasksWarning.setVisible(true);
                    listOfCourses.setDisable(true);
                }
            }
        }
    }

    private void postTasks(ArrayList<Task> tasks, String courseId) throws IOException {
        for (int i = 0; i < tasks.size(); i++){
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(tasks.get(i));
            Https.sendJson(user.getId(),user.getPassword(),"POST",Controller.PROFESSOR, URL_COURSES +"/" + courseId + "/tasks", jsonResponse);
        }
    }


    private void goToSetTasks(ArrayList<Task> tasks, String courseId, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_tasks_properties.fxml"));
        Parent root = loader.load();
        SetTasksPropertiesController sapc = loader.getController();
        sapc.setTasks(user, tasks, Integer.valueOf(courseId));
        SceneController.switchToScene(event, root);
    }

    private ArrayList<Task> makeEmptyTasks(Integer numberOfTasks) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= numberOfTasks; i++) {
            Task temp = new Task(i, DEFAULT_DATE, DEFAULT_DATE, DEFAULT_PERCENTAGE);
            tasks.add(temp);
        }
        return tasks;
    }

    @FXML
    void backButtonPressed(ActionEvent event) {
        if (listOfCourses.isDisabled()) {
            numberOfTasksPicker.setVisible(false);
            numberOfTasksLabel.setVisible(false);
            numberOfTasksWarning.setVisible(false);
            listOfCourses.setDisable(false);
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


    public int setProfessor(Professor user) throws IOException {
        this.user = user;
        professorCourses = Controller.initializeController(user,listOfCourses);
        if (professorCourses != null) {
            ObservableList<Integer> numberOfCourses = FXCollections.observableArrayList(3, 4, 5, 6);
            numberOfTasksPicker.setItems(numberOfCourses);
            return CREATED;
        }
        else{
            return FAILED_TO_CREATE;
        }
    }
}
