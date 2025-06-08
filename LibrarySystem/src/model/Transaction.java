package model;

public class Transaction {
    private String id;
    private String memberId;
    private String isbn;
    private String borrowDate;
    private String returnDate;
    private String status;

    public Transaction(String id, String memberId, String isbn, String borrowDate, String returnDate, String status) {
        this.id = id;
        this.memberId = memberId;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getId() { return id; }
    public String getMemberId() { return memberId; }
    public String getIsbn() { return isbn; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}
