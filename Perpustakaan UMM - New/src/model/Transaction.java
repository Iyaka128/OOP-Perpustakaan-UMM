package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_DATE;

    private String id;
    private String memberId;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;          // BORROWED | RETURNED
    private long fine;              // total fine in rupiah

    public Transaction(String id, String memberId, String isbn,
                       LocalDate borrowDate, LocalDate returnDate,
                       String status, long fine) {
        this.id = id;
        this.memberId = memberId;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fine = fine;
    }

    // ctor for CSV load
    public Transaction(String[] parts) {
        this(parts[0], parts[1], parts[2],
                LocalDate.parse(parts[3]), LocalDate.parse(parts[4]),
                parts[5], Long.parseLong(parts[6]));
    }

    // ── Getters / Setters ─────────────────────────────────────────────
    public String  getId()        { return id; }
    public String  getMemberId()  { return memberId; }
    public String  getIsbn()      { return isbn; }
    public LocalDate getBorrowDate(){return borrowDate;}
    public LocalDate getReturnDate(){return returnDate;}
    public String  getStatus()    { return status; }
    public long    getFine()      { return fine; }

    public void setStatus(String status){ this.status = status; }
    public void setFine(long fine)      { this.fine = fine;     }

    // ── CSV ───────────────────────────────────────────────────────────
    public String toCSV() {
        return String.join(",",
                id, memberId, isbn,
                DF.format(borrowDate), DF.format(returnDate),
                status, String.valueOf(fine));
    }

    public void setReturnDate(LocalDate now) {
    }
}
