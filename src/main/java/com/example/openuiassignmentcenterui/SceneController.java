package com.example.openuiassignmentcenterui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switchToScene(ActionEvent event, Parent fxml) throws IOException{
        /*
        * TODO: Add optional variables, that allow to pass on relevant varibales when changing fxml if possible.
         */
        root = fxml;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
