package com.andrade.commentremover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
 
public class App extends Application {
    @Override
        public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/andrade/commentremover/view/Main.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
 public static void main(String[] args) {
        launch(args);
    }
}
