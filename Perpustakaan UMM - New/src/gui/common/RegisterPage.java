package gui.common;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Member;
import service.LibraryManager;
import gui.util.AlertUtil;

public class RegisterPage {
    private final LibraryManager manager;
    private final Stage stage;

    public RegisterPage(LibraryManager manager, Stage stage) {
        this.manager = manager;
        this.stage = stage;
    }

    public VBox getView() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String pass = passField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                AlertUtil.error("Error", "All fields must be filled.");
                return;
            }

            if (manager.emailExists(email)) {
                AlertUtil.error("Error", "Email already exists.");
                return;
            }

            manager.addMember(new Member(id, name, email, pass, "member"));
            AlertUtil.info("Success", "Member registered.");
            stage.setScene(new Scene(new LoginPage(manager, stage).getView(), 600, 420));
        });

        box.getChildren().addAll(idField, nameField, emailField, passField, registerBtn);
        return box;
    }
}
