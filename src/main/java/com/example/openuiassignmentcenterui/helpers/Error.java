package com.example.openuiassignmentcenterui.helpers;

import javafx.scene.control.Alert;

public class Error {
    private final String title;
    private final String message;

    public Error(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public Error() {
        this.title = "Generic Error";
        this.message = "Dimwit, you did something wrong, figure it out!";
    }

    public void raiseError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
