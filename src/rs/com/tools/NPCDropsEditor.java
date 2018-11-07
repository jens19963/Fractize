package com.rs.tools;

/**
 * Created by Peng on 24.2.2016 22:01.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NPCDropsEditor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("NPC Drops Editor V0.1");
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("./Editor.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        EditorController controller = fxmlLoader.getController();
        primaryStage.setScene(new Scene(root, 600.0D, 400.0D));
        primaryStage.show();
        primaryStage.getScene().getWindow().setOnCloseRequest((e) -> {
            controller.doClose();
        });
        controller.init();
        primaryStage.show();
    }
}
