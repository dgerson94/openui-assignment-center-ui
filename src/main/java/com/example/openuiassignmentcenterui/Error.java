package com.example.openuiassignmentcenterui;

import javafx.scene.control.Alert;

public class Error {
    private String title;
    private String message;
    public Error(String title, String message){
        this.title = title;
        this.message = message;
    }

    public Error(){
        this.title = "Genereic Error";
        this.message = "Dimwit, you did somethign wrong, figure it out!";
    }

    public void raiseError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
