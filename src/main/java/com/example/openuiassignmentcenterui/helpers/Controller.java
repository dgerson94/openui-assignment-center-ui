package com.example.openuiassignmentcenterui.helpers;

import com.example.openuiassignmentcenterui.controllers.ProfessorDashboardController;
import com.example.openuiassignmentcenterui.controllers.SceneController;
import com.example.openuiassignmentcenterui.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    public static final String URL_COURSES = "http://localhost:8080/courses";
    public static final String STUDENT = "student";
    public static final String PROFESSOR = "professor";

    public static ArrayList<Course> initializeController(Professor user, ListView<String> listOfCourses) throws IOException {
        String user_name = user.getId();
        String password = user.getPassword();
        StringBuffer response = Https.httpGet(user_name, password, Controller.PROFESSOR, URL_COURSES);
        TypeToken<ArrayList<Course>> courseType = new TypeToken<>() {
        };
        ArrayList<Course> professorCourses = new Gson().fromJson(String.valueOf(response), courseType);
        ArrayList<String> nameOfCourses = getNameOfCourses(professorCourses);

        ObservableList<String> courses = FXCollections.observableArrayList(nameOfCourses);
        listOfCourses.setItems(courses);
        return professorCourses;
    }

    public static ArrayList<String> getNameOfCourses(ArrayList<Course> professorCourses) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < professorCourses.size(); i++) {
            list.add(capitalize(professorCourses.get(i).getName()));
        }
        return list;
    }

    public static String capitalize(String str) {
        String capitalizedStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        return capitalizedStr;
    }

    public static String getCourseId(String picked, ArrayList<Course> courses) {
        String str = null;
        for (int i = 0; i < courses.size(); i++) {
            if (capitalize(courses.get(i).getName()).matches(picked)) {
                str = courses.get(i).getId().toString();
            }
        }
        return str;
    }

    public static String getTaskId(String pickedTask, ArrayList<Task> tasks) {
        String str = null;
        for (int i = 0; i < tasks.size(); i++) {
            String taskId = tasks.get(i).getId().toString();
            if (("Assignment " + taskId).matches(pickedTask)) {
                str = taskId;
            }
        }
        return str;
    }

    public static ObservableList<String> createObservableList(Integer size) {
        ObservableList<String> oList = FXCollections.observableArrayList();
        int count = 1;
        for (int i = 0; i < size; i++) {
            oList.add("Assignment " + count);
            count++;
        }
        return oList;
    }

    public static ObservableList<String> createObservableStudentList(ArrayList<Submission> submissions) {
        ObservableList<String> oList = FXCollections.observableArrayList();
        for (int i = 0; i < submissions.size(); i++) {
            oList.add(submissions.get(i).getStudentId());
        }
        return oList;
    }

    public static Professor createUser (String user_name, String password){
        Professor professor = new Professor(user_name,password);
        return professor;
    }

    public static void update_lists_forward(ListView<String> newList, ListView<String> oldList, ObservableList<String> newOList) {
        newList.setItems(newOList);
        newList.refresh();
        newList.setDisable(false);
        oldList.setDisable(true);
    }

    public static void update_lists_backwards(ListView<String> newList, ListView<String> oldList){
        oldList.setItems(null);
        oldList.refresh();
        oldList.setDisable(true);
        newList.setDisable(false);
    }

    public static String readText(File tmp) {
        try (BufferedReader br = new BufferedReader(new FileReader(tmp))) {
            String text = "";
            String str = "";
            String line;
            while ((line = br.readLine()) != null) {
                text = str.concat(line);
            }
            return text;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return "Error reading file. Please try again.";
    }
}


