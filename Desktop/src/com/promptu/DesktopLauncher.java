package com.promptu;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Guy on 18/11/2016.
 */
public class DesktopLauncher extends Application {

    public static void main(String[] args) {
        launch(DesktopLauncher.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Active Pane");
        Parent root = FXMLLoader.load(getClass().getResource("display/activePane.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("display/activePane.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
