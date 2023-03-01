package com.example.openuiassignmentcenterui.controllers;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;


public class SetCourseRequirementsController {
    private Professor user;

    private ArrayList<Course> professorCourses;

    @FXML
    private ListView<String> listOfCourses;

    @FXML
    private ChoiceBox<Integer> numberOfAssignmentsPicker;

    @FXML
    private Label numberOfAssignmentsLabel;

    @FXML
    void continueButtonPressed(ActionEvent event) throws IOException {
        String pickedCourse = listOfCourses.getSelectionModel().getSelectedItem();
        if (pickedCourse == null) {
            Error e = new Error("No course selected", "You did not select a course, please select a course.");
            e.raiseError();
        }
        else {
            String courseId =  getCourseId(pickedCourse);
            StringBuffer response = Https.httpGet(user.getId(),user.getPassword(),Https.PROFESSOR,"http://localhost:8080/courses/" + courseId + "/tasks");
            if (!response.toString().equals("[]")){
                TypeToken <ArrayList<Task>> courseType = new TypeToken<>() {
                };
                ArrayList<Task> tasks = new Gson().fromJson(String.valueOf(response), courseType);
                goToSetAssignments(tasks, courseId, event);
            }
            else{
                if (numberOfAssignmentsPicker.isVisible()) {
                    if (numberOfAssignmentsPicker.getValue() == null) {
                        Error e = new Error("No number selected.", "You did not select how many assignments will be in the course. Please select how many there will be.");
                        e.raiseError();
                    } else {
                        Integer numberOfAssignments = numberOfAssignmentsPicker.getValue();
                        ArrayList<Task> tasks = makeEmptyTasks(numberOfAssignments);
                        goToSetAssignments(tasks, courseId, event);
                    }
                }
                else{
                    numberOfAssignmentsPicker.setVisible(true);
                    numberOfAssignmentsLabel.setVisible(true);
                }
            }

        }


    }

    private void goToSetAssignments(ArrayList<Task> tasks, String courseId, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_assignments_properties.fxml"));
        Parent root = loader.load();
        SetAssignmentsPropertiesController sapc = loader.getController();
        sapc.setTasks(user,tasks,Integer.valueOf(courseId));
        SceneController.switchToScene(event, root);
    }

    private ArrayList<Task> makeEmptyTasks(Integer numberOfAssignments) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= numberOfAssignments; i++){
            Task temp = new Task(String.valueOf(i), new Date(), new Date(), 0.1);
            tasks.add(temp);
        }
        return tasks;
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("professor_dashboard.fxml"));
        Parent root = loader.load();
        ProfessorDashboardController pdc = loader.getController();
        pdc.setUser(user);
        SceneController.switchToScene(event, root);
    }

    public void setProfessor(Professor user) throws IOException {
        this.user = user;
        String user_name = user.getId();
        String password = user.getPassword();
        StringBuffer response = Https.httpGet(user_name, password, Https.PROFESSOR, "http://localhost:8080/courses");

        TypeToken <ArrayList<Course>> courseType = new TypeToken<>() {
        };
        professorCourses = new Gson().fromJson(String.valueOf(response), courseType);
        ArrayList<String> nameOfCourses = getNameOfCourses(professorCourses);

        ObservableList<String> courses = FXCollections.observableArrayList(nameOfCourses);
        ObservableList<Integer> numberOfCourses = FXCollections.observableArrayList(3, 4, 5, 6);

        listOfCourses.setItems(courses);
        numberOfAssignmentsPicker.setItems(numberOfCourses);
    }

    private ArrayList<String> getNameOfCourses(List<Course> professorCourses) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < professorCourses.size(); i++){
            list.add(capitalize(professorCourses.get(i).getName()));
        }
        return list;
    }

    private static String capitalize(String str){
        String capitalizedStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        return capitalizedStr;
    }

    private String getCourseId(String picked){
        String str = null;
        for (int i = 0; i < professorCourses.size(); i++){
            if (capitalize(professorCourses.get(i).getName()).matches(picked)) {
                str = professorCourses.get(i).getId().toString();
            }
        }
        return str;
    }

}
