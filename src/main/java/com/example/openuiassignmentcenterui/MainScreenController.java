package com.example.openuiassignmentcenterui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainScreenController {

    @FXML
    void proffesorButtonPressed(ActionEvent event) {
        System.out.println("This Works Proffesor");
    }

    @FXML
    void studentButtonPressed(ActionEvent event) {
        System.out.println("This Works Student");
    }

}
