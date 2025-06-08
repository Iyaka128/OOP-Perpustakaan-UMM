package main;

import gui.SimpleView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.LibraryManager;

public class LibraryApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LibraryManager manager = new LibraryManager();
        SimpleView simpleView = new SimpleView(manager);

        Scene scene = new Scene(simpleView.getView(), 500, 400);
        primaryStage.setTitle("Simple Library GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
