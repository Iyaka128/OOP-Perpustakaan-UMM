package gui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

public class SimpleView {
    private VBox view;

    public SimpleView(LibraryManager manager) {
        view = new VBox(10);
        Label label = new Label("Library System (Simple Backend Test)");

        Button btnAddSample = new Button("Add Sample Book & Borrow");
        TextArea output = new TextArea();
        output.setEditable(false);

        btnAddSample.setOnAction(e -> {
            Book book = new Book("123", "Java Dasar", "Agus", 5);
            Member member = new Member("M001", "Budi", "Informatics", "budi@umm.ac.id");
            manager.addBook(book);
            manager.addMember(member);
            manager.borrowBook("T001", "M001", "123", "2025-06-08", "2025-06-15");

            StringBuilder sb = new StringBuilder();
            sb.append("Books:\n");
            for (Book b : manager.getBooks()) sb.append(b).append("\n");
            sb.append("Members:\n");
            for (Member m : manager.getMembers()) sb.append(m).append("\n");
            sb.append("Transactions:\n");
            for (Transaction t : manager.getTransactions()) sb.append(t.getId()).append(" - ").append(t.getStatus()).append("\n");

            output.setText(sb.toString());
        });

        view.getChildren().addAll(label, btnAddSample, output);
    }

    public VBox getView() {
        return view;
    }
}
