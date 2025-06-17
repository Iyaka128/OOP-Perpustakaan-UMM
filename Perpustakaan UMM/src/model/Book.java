package model;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String category;
    private int quantity;
    private String status;

    public Book(String isbn, String title, String author, String category, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
    }

    public void updateStatus() {
        this.status = (quantity > 0) ? "Available" : "Out of Stock";
    }

    public String getStatus() {
        return status;
    }


    // ── Getters / Setters ─────────────────────────────────────────────
    public String getIsbn()      { return isbn;     }
    public String getTitle()     { return title;    }
    public String getAuthor()    { return author;   }
    public String getCategory()  { return category; }
    public int    getQuantity()  { return quantity; }

    public void setTitle(String title)         { this.title = title;       }
    public void setAuthor(String author)       { this.author = author;     }
    public void setCategory(String category)   { this.category = category; }
    public void setQuantity(int quantity)      { this.quantity = quantity; }

    // ── CSV representation ────────────────────────────────────────────
    public String toCSV() {
        return String.join(",", isbn, title, author, category, String.valueOf(quantity));
    }

    @Override public String toString() { return title + " – " + author; }
}
