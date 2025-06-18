package gui.transaction;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.Book;
import model.Member;
import model.Transaction;
import service.LibraryManager;
import gui.common.LoginPage;
import gui.util.AlertUtil;

import java.util.List;

public class TransactionPage {
    private final LibraryManager manager;
    private final Stage stage;
    private final Member member;
    private TableView<Transaction> transactionTable;
    private TableView<Book> availableBookTable;
    private FilteredList<Book> filteredBooks;

    public TransactionPage(LibraryManager manager, Stage stage, Member member) {
        this.manager = manager;
        this.stage = stage;
        this.member = member;
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F5F5;");

        // HEADER
        HBox header = new HBox();
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #A8E6CF;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label welcomeLabel = new Label("Welcome, " + member.getName());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        logoutBtn.setOnAction(e -> stage.setScene(new Scene(new LoginPage(manager, stage).getView(), 600, 400)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(welcomeLabel, spacer, logoutBtn);

        // TRANSACTION TABLE
        transactionTable = new TableView<>();
        transactionTable.setItems(FXCollections.observableArrayList(manager.getMemberTransactions(member.getId())));

        TableColumn<Transaction, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Transaction, String> colBorrow = new TableColumn<>("Borrow");
        colBorrow.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        TableColumn<Transaction, String> colReturn = new TableColumn<>("Return");
        colReturn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        TableColumn<Transaction, Integer> colFine = new TableColumn<>("Fine");
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));

        transactionTable.getColumns().addAll(colId, colIsbn, colBorrow, colReturn, colFine);
        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ACTION BUTTONS
        Button borrowBtn = new Button("Borrow Selected Book");
        borrowBtn.setStyle("-fx-background-color: #A8E6CF; -fx-effect: dropshadow(one-pass-box, gray, 3, 0, 1, 1);");
        borrowBtn.setOnAction(e -> {
            Book selected = availableBookTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                boolean success = manager.borrowBook(member.getId(), selected.getIsbn());
                if (success) {
                    AlertUtil.info("Success", "Book borrowed successfully.");
                    refreshTables();
                } else {
                    AlertUtil.error("Failed", "Cannot borrow book. Maybe it's out of stock or already borrowed.");
                }
            } else {
                AlertUtil.error("No Book Selected", "Please select a book to borrow.");
            }
        });

        Button newBookBtn = new Button("New Book");
        newBookBtn.setStyle("-fx-background-color: #A8E6CF; -fx-effect: dropshadow(one-pass-box, gray, 3, 0, 1, 1);");
        newBookBtn.setOnAction(e -> showAddBookDialog());



        Button returnBtn = new Button("Return Selected");
        returnBtn.setStyle("-fx-background-color: #A8E6CF; -fx-effect: dropshadow(one-pass-box, gray, 3, 0, 1, 1);");
        returnBtn.setOnAction(e -> {
            Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                boolean returned = manager.returnBook(selected);
                if (returned) {
                    refreshTables(); // Ini penting agar status di UI berubah
                } else {
                    AlertUtil.error("Already Returned", "This book was already returned.");
                }
            } else {
                AlertUtil.error("No Transaction Selected", "Please select a transaction to return.");
            }
        });



        HBox actionButtons = new HBox(10, borrowBtn, returnBtn, newBookBtn);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(10));

        // SEARCH & AVAILABLE BOOKS
        Label searchLabel = new Label("Available Books");
        TextField searchField = new TextField();
        searchField.setPromptText("Search Books …");

        filteredBooks = new FilteredList<>(FXCollections.observableArrayList(manager.getAvailableBooks()), p -> true);
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            filteredBooks.setPredicate(book -> book.getTitle().toLowerCase().contains(newV.toLowerCase()));
        });

        availableBookTable = new TableView<>();
        availableBookTable.setItems(filteredBooks);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Book, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        availableBookTable.getColumns().addAll(isbnCol, titleCol, authorCol, catCol, qtyCol);
        availableBookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox searchSection = new VBox(5, searchLabel, searchField, availableBookTable);
        searchSection.setPadding(new Insets(10));

        // LAYOUT
        VBox center = new VBox(10, transactionTable, actionButtons, searchSection);
        center.setPadding(new Insets(10));

        root.setTop(header);
        root.setCenter(center);
        return root;
    }

    private void showAddBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");

        // Tombol OK dan Cancel
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        TextField qtyField = new TextField();
        qtyField.setPromptText("Quantity");

        grid.add(new Label("ISBN:"), 0, 0);
        grid.add(isbnField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(authorField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Quantity:"), 0, 4);
        grid.add(qtyField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convert result to Book when OK clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int quantity = Integer.parseInt(qtyField.getText().trim());
                    return new Book(
                            isbnField.getText().trim(),
                            titleField.getText().trim(),
                            authorField.getText().trim(),
                            categoryField.getText().trim(),
                            quantity
                    );
                } catch (NumberFormatException e) {
                    AlertUtil.error("Invalid Input", "Quantity must be a number.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(book -> {
            manager.addBook(book);       // Tambahkan ke manager
            refreshTables();             // Segarkan tampilan
        });
    }



    private void refreshTables() {
        transactionTable.setItems(FXCollections.observableArrayList(manager.getMemberTransactions(member.getId())));
        availableBookTable.setItems(FXCollections.observableArrayList(manager.getAvailableBooks()));
    }
}
