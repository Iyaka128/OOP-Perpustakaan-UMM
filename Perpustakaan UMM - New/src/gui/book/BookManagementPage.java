package gui.book;

import gui.util.AlertUtil;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Book;
import service.LibraryManager;
import javafx.scene.layout.VBox;


public class BookManagementPage {
    private final LibraryManager manager;
    private final Stage stage;
    private TableView<Book> table;
    private ObservableList<Book> data;

    public BookManagementPage(LibraryManager manager, Stage stage){
        this.manager = manager;
        this.stage   = stage;
    }

    public BorderPane getView(){
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        /* ===== Search bar ===== */
        TextField searchF = new TextField(); searchF.setPromptText("Search…");
        searchF.textProperty().addListener((obs,ov,nv)->refresh(nv));

        Button addBtn    = new Button("Add");
        Button editBtn   = new Button("Edit");
        Button delBtn    = new Button("Delete");
        Button backBtn   = new Button("← Logout");

        HBox top = new HBox(8, searchF, addBtn, editBtn, delBtn, backBtn);
        root.setTop(top);

        /* ===== Table ===== */
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(col("ISBN",Book::getIsbn),
                col("Title",Book::getTitle),
                col("Author",Book::getAuthor),
                col("Cat.",Book::getCategory),
                col("Qty", b->String.valueOf(b.getQuantity())));
        refresh("");

        root.setCenter(table);

        /* ===== handlers ===== */
        addBtn.setOnAction(e->openForm(null));
        editBtn.setOnAction(e->{
            Book sel = table.getSelectionModel().getSelectedItem();
            if(sel==null){AlertUtil.error("Err","Select a row");return;}
            openForm(sel);
        });
        delBtn.setOnAction(e->{
            Book sel = table.getSelectionModel().getSelectedItem();
            if(sel==null) return;
            if(AlertUtil.confirm("Delete","Sure?")){
                manager.deleteBook(sel); refresh("");
            }
        });
        backBtn.setOnAction(e-> stage.close());   // simple logout

        return root;
    }

    private <T> TableColumn<Book,String> col(String t, java.util.function.Function<Book,String> m){
        TableColumn<Book,String> c=new TableColumn<>(t);
        c.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(m.apply(d.getValue())));
        return c;
    }

    private void refresh(String key){
        data = FXCollections.observableArrayList(key.isEmpty()?manager.getAvailableBooks():manager.searchBooks(key));
        table.setItems(data);
    }

    /* ===== Form dialog ===== */
    private void openForm(Book book){
        Dialog<Book> d = new Dialog<>();
        d.setTitle(book==null?"Add Book":"Edit Book");
        TextField isbnF = new TextField(); isbnF.setPromptText("ISBN");
        TextField titF  = new TextField(); titF.setPromptText("Title");
        TextField autF  = new TextField(); autF.setPromptText("Author");
        TextField catF  = new TextField(); catF.setPromptText("Category");
        TextField qtyF  = new TextField(); qtyF.setPromptText("Quantity");

        if(book!=null){
            isbnF.setText(book.getIsbn()); isbnF.setDisable(true);
            titF.setText(book.getTitle());
            autF.setText(book.getAuthor());
            catF.setText(book.getCategory());
            qtyF.setText(String.valueOf(book.getQuantity()));
        }
        d.getDialogPane().setContent(new VBox(8,isbnF,titF,autF,catF,qtyF));
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);

        d.setResultConverter(bt->{
            if(bt==ButtonType.OK){
                try{
                    int q=Integer.parseInt(qtyF.getText());
                    if(book==null){
                        manager.addBook(new Book(isbnF.getText(),titF.getText(),autF.getText(),catF.getText(),q));
                    }else{
                        book.setTitle(titF.getText());
                        book.setAuthor(autF.getText());
                        book.setCategory(catF.getText());
                        book.setQuantity(q);
                        manager.updateBook(book);
                    }
                    refresh("");
                }catch(NumberFormatException e){AlertUtil.error("Err","Qty must be int");}
            }
            return null;
        });
        d.showAndWait();
    }
}
