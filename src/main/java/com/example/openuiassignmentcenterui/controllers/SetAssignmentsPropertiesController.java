package com.example.openuiassignmentcenterui.controllers;
import com.example.openuiassignmentcenterui.helpers.Https;
import com.example.openuiassignmentcenterui.models.Professor;
import com.example.openuiassignmentcenterui.models.Task;
import com.google.gson.Gson;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SetAssignmentsPropertiesController {

    private ArrayList<Task> tasks;

    private Professor user;

    private File file;

    private Integer courseId;
    @FXML
    private Label Title;

    private Task currentTask;

    @FXML
    private DatePicker assignmentDueDatePicker;

    @FXML
    private TextField assignmentFileTextField;

    @FXML
    private ListView<String> assignmentList;

    @FXML
    private DatePicker gradeDueDatePicker;

    @FXML
    private Slider percentageOfCourseGradeSlider;

    @FXML
    private Button uploadFileButton;


    @FXML
    void uploadFileButtonPressed(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Assignment File");
        file = fileChooser.showOpenDialog(stage);
        assignmentFileTextField.setText(file.getName());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_course_requirements.fxml"));
        Parent root = loader.load();
        SetCourseRequirementsController scrc = loader.getController();
        scrc.setProfessor(user);
        SceneController.switchToScene(event, root);
    }

    @FXML
    void editButtonPressed(ActionEvent event) {
        String pickedTaskName = assignmentList.getSelectionModel().getSelectedItem();
        Task pickedTask = getTask(Integer.valueOf(pickedTaskName.substring(pickedTaskName.length() - 1)));
        setDisable(percentageOfCourseGradeSlider, gradeDueDatePicker, assignmentDueDatePicker, uploadFileButton ,false);
    }

    private Task getTask(Integer pickedTaskName) {
        Task ret = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == pickedTaskName)
                ret = tasks.get(i);
        }
        return ret;
    }

    @FXML
    void saveButtonPressed(ActionEvent event) throws IOException {
        //TODO: Need to have two different actions - One for saving all info in Json and one for sending File.
        currentTask.setCheckDeadLine(String.valueOf(gradeDueDatePicker.getValue()));
        currentTask.setSubmissionDeadline(String.valueOf(assignmentDueDatePicker.getValue()));
        currentTask.setWeightInGrade(percentageOfCourseGradeSlider.getValue());
        sendJson();
        sendFile();
        setDisable(percentageOfCourseGradeSlider, gradeDueDatePicker, assignmentDueDatePicker, uploadFileButton, true);
    }

    private void sendFile() throws IOException {
        Https.httpPutFile(user.getId(), user.getPassword(), null, "http://localhost:8080/courses/1/tasks/1/file", file);
    }

    private void sendJson() throws IOException {
        Task tmp = new Task(currentTask.getId(), currentTask.getSubmissionDeadline(), currentTask.getCheckDeadLine(), currentTask.getWeightInGrade()/100);
        //tmp.setCourseId(courseId);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(tmp);
        StringBuffer response = Https.httpPutJson(user.getId(), user.getPassword(), null, "http://localhost:8080/courses/1/tasks/1", jsonResponse);
    }

    private ObservableList<String> createObservableList(Integer size) {
        ObservableList<String> oList = FXCollections.observableArrayList();
        int count = 1;
        for (int i = 0; i < size; i++) {
            oList.add("Assignment " + count);
            count++;
        }
        return oList;
    }

    public void setTasks(Professor user, ArrayList<Task> tasks, Integer courseId) throws IOException {
        this.user = user;
        this.tasks = tasks;
        this.courseId = courseId;
        this.file = null;
        ObservableList<String> assignments = createObservableList(tasks.size());
        assignmentList.setItems(assignments);
        onItemSelected(assignmentList);
    }


    private LocalDate makeLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); // the format of the input string
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    private Task getCurrentTask(String currentTaskString) {
        Task task = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (("Assignment " + tasks.get(i).getId()).matches(currentTaskString)) {
                task = tasks.get(i);
            }
        }
        return task;
    }

    private void onItemSelected(ListView<String> listView) {
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String oldVal, String newVal) -> {
                    if (newVal != null) {
                        String currentTaskString = assignmentList.getSelectionModel().getSelectedItem();
                        currentTask = getCurrentTask(currentTaskString);
                        percentageOfCourseGradeSlider.adjustValue(currentTask.getWeightInGrade() * 100);
                        gradeDueDatePicker.setValue(makeLocalDate(currentTask.getCheckDeadLine()));
                        assignmentDueDatePicker.setValue(makeLocalDate(currentTask.getSubmissionDeadline()));
                        try {
                           file = Https.httpGetFile(user.getId(), user.getPassword(), null, "http://localhost:8080/courses/1/tasks/1/file");
                           assignmentFileTextField.setText(file.getName());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        setDisable(percentageOfCourseGradeSlider, gradeDueDatePicker, assignmentDueDatePicker, uploadFileButton, true);
                    }
                });
    }

    private void setDisable(Slider s, DatePicker dp1, DatePicker dp2, Button btn, Boolean b) {
        s.setDisable(b);
        dp1.setDisable(b);
        dp2.setDisable(b);
        btn.setDisable(b);
        //TODO: add a disable to file finder.
    }
}