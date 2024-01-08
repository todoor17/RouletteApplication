package sample.bettingproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("startPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
/*
        stage.initStyle(StageStyle.UNDECORATED);
*/
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        System.out.println();
    }

    public static void main(String[] args) {
        launch();
    }
}
