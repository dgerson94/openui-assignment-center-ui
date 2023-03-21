package com.example.openuiassignmentcenterui.helpers;

import com.example.openuiassignmentcenterui.models.Course;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Student;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    public static final String URL_COURSES = "http://localhost:8080/courses";

    public static ArrayList<Course> initializeController(Professor user, ListView<String> listOfCourses) throws IOException {
        String user_name = user.getId();
        String password = user.getPassword();
        StringBuffer response = Https.httpGet(user_name, password, Https.PROFESSOR, URL_COURSES);
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

    public static ObservableList<String> createObservableStudentList(ArrayList<Student> students) {
        ObservableList<String> oList = FXCollections.observableArrayList();
        for (int i = 0; i < students.size(); i++) {
            oList.add(students.get(i).getId());
        }
        return oList;
    }
}


