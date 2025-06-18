package gui.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.Member;
import service.LibraryManager;
import gui.transaction.TransactionPage;
import gui.book.BookManagementPage;
import gui.util.AlertUtil;



public class LoginPage {
    private final LibraryManager manager;
    private final Stage stage;

    public LoginPage(LibraryManager manager, Stage stage) {
        this.manager = manager;
        this.stage = stage;
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();

        // LEFT (Branding)
        VBox leftBox = new VBox(10);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPadding(new Insets(20));
        leftBox.setStyle("-fx-background-color: #A8C4B1;"); // soft green

        Text title = new Text("📚 Campus Library");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        title.setFill(Color.DIMGRAY);

        Text subTitle = new Text("PERPUSTAKAAN\nUMM");
        subTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
        subTitle.setTextAlignment(TextAlignment.CENTER);
        subTitle.setFill(Color.BLACK);

        leftBox.getChildren().addAll(title, subTitle);

        // RIGHT (Form)
        VBox rightBox = new VBox(10);
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPadding(new Insets(20));
        rightBox.setStyle("-fx-background-color: #F5F5F5;");

        TextField idField = new TextField();
        idField.setPromptText("ID (admin/M001 …)");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: lightgray;");
        loginBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String pass = passField.getText().trim();

            if (id.equals("admin") && pass.equals("admin")) {
                stage.setScene(new Scene(new BookManagementPage(manager, stage).getView(), 800, 500));
            } else {
                manager.validateLogin(id, pass).ifPresentOrElse(
                        member -> stage.setScene(new Scene(new TransactionPage(manager, stage, member).getView(), 800, 500)),
                        () -> AlertUtil.error("Login Failed", "Invalid ID or Password")
                );
            }
        });

        Button registerBtn = new Button("Register New Member");
        registerBtn.setStyle("-fx-background-color: lightgray;");
        registerBtn.setOnAction(e ->
                stage.setScene(new Scene(new RegisterPage(manager, stage).getView(), 500, 400))
        );

        rightBox.getChildren().addAll(idField, passField, loginBtn, registerBtn);

        // Split layout
        // Bungkus rightBox agar berada di tengah-tengah bidang kanan
        StackPane rightWrapper = new StackPane(rightBox);
        rightWrapper.setAlignment(Pos.CENTER); // Pusatkan vertikal & horizontal
        rightWrapper.setPrefWidth(450);        // Atur lebar kanan jika perlu

        HBox mainLayout = new HBox(leftBox, rightWrapper);

        root.setCenter(mainLayout);

        return root;
    }
}
