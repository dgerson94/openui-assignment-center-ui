package com.example.openuiassignmentcenterui.helpers;

import javafx.scene.control.Alert;


public class Error {
    private final String title;
    private final String message;

    public static final String IO_TITLE = "IOException Error";
    public static final String IO_MESSAGE = "An IOException Error occurred. Program will close now and please restart and try again";

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

    public static void ioError(){
        Error err = new Error(Error.IO_TITLE,Error.IO_MESSAGE);
        err.raiseError();
        System.exit(1);
    }
}
