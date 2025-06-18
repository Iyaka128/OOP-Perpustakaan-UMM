package main;

import gui.common.LoginPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.LibraryManager;

public class  LibraryApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // ── Service layer ───────────────────────────────────────────────
        LibraryManager manager = new LibraryManager();   // load CSV otomatis
        // ── GUI layer ───────────────────────────────────────────────────
        LoginPage login = new LoginPage(manager, primaryStage);
        Scene scene   = new Scene(login.getView(), 600, 420);

        primaryStage.setTitle("Campus Library System – Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
