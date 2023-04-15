package com.example.openuiassignmentcenterui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private static Stage stage;
    private static Scene scene;

    private SceneController() {
        throw new IllegalStateException("SceneController class");
    }

    public static void switchToScene(ActionEvent event, String fxml) throws IOException{
        Parent root = FXMLLoader.load(SceneController.class.getResource(fxml));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void switchToScene(ActionEvent event, Parent root){
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
