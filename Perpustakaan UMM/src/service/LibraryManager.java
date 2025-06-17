package service;

import dao.CSVHelper;
import model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryManager {

    private static final String BOOK_FILE  = "data/books.csv";
    private static final String MEM_FILE   = "data/members.csv";
    private static final String TX_FILE    = "data/transactions.csv";
    private static final long   FINE_PER_DAY = 1000;          // Rp 1.000 / hari

    private final List<Book> books;
    private final List<Member> members;
    private final List<Transaction> transactions;

    public LibraryManager() {
        books        = CSVHelper.readBooks(BOOK_FILE);
        members      = CSVHelper.readMembers(MEM_FILE);
        transactions = CSVHelper.readTx(TX_FILE);

        // Tambahkan data dummy jika data kosong (misalnya saat pertama kali)
        if (books.isEmpty()) {
            books.add(new Book("978-602-03-1234-5", "Pemrograman Java", "A. Nugroho", "Pemrograman", 3));
            books.add(new Book("978-979-29-1234-6", "Struktur Data", "B. Prasetyo", "Ilmu Komputer", 2));
            books.add(new Book("978-602-02-1234-7", "Dasar-Dasar Algoritma", "C. Santosa", "Algoritma", 5));
            saveBooks();
        }

        if (members.isEmpty()) {
            members.add(new Member("M001", "Iyaka", "iyaka@example.com", "123", "MEMBER"));
            members.add(new Member("M002", "Dina", "dina@example.com", "123", "MEMBER"));
            CSVHelper.writeMembers(MEM_FILE, members);
        }

    }

    /* ========= BOOK CRUD & SEARCH ========= */
    // Tambahkan di dalam LibraryManager.java
    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(book -> book.getQuantity() > 0)
                .collect(Collectors.toList());
    }


    public void addBook(Book b){
        books.add(b);
        saveBooks();
    }
    public void updateBook(Book b){ saveBooks(); }
    public void deleteBook(Book b){
        books.remove(b);
        saveBooks();
    }
    public List<Book> searchBooks(String keyword){
        String key = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getIsbn().toLowerCase().contains(key)
                        || b.getTitle().toLowerCase().contains(key)
                        || b.getAuthor().toLowerCase().contains(key)
                        || b.getCategory().toLowerCase().contains(key))
                .collect(Collectors.toList());
    }

    /* ========= MEMBER ========= */
    public List<Member> getMembers(){ return members; }
    public boolean emailExists(String email){
        return members.stream().anyMatch(m -> m.getEmail().equalsIgnoreCase(email));
    }
    public void addMember(Member m){
        members.add(m);
        CSVHelper.writeMembers(MEM_FILE, members);
    }
    public Optional<Member> validateLogin(String id, String pass){
        return members.stream()
                .filter(m -> m.getId().equals(id) && m.getPassword().equals(pass))
                .findFirst();
    }

    /* ========= TRANSACTIONS ========= */
    public List<Transaction> getMemberTransactions(String memberId) {
        return transactions.stream()
                .filter(t -> t.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    public boolean borrowBook(String memberId, String isbn) {
        Optional<Book> bookOpt = books.stream()
                .filter(b -> b.getIsbn().equals(isbn) && b.getQuantity() > 0)
                .findFirst();

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setQuantity(book.getQuantity() - 1);

            String txId = UUID.randomUUID().toString();
            Transaction tx = new Transaction(txId, memberId, isbn, LocalDate.now(), null, "Borrowed", 0);
            transactions.add(tx);
            return true;
        }
        return false;
    }


    public void returnBook(Transaction tx){
        if(!"BORROWED".equals(tx.getStatus())) return;
        LocalDate today = LocalDate.now();
        long lateDays = Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(tx.getReturnDate(), today));
        long fine = lateDays * FINE_PER_DAY;
        tx.setFine(fine);
        tx.setStatus("RETURNED");

        // restore quantity
        books.stream()
                .filter(b -> b.getIsbn().equals(tx.getIsbn()))
                .findFirst()
                .ifPresent(b -> {
                    b.setQuantity(b.getQuantity()+1);
                    b.updateStatus();
                });


        saveAll();
    }

    /* ========= PRIVATE SAVE ========= */
    private void saveBooks(){ CSVHelper.writeBooks(BOOK_FILE, books); }
    private void saveTx()   { CSVHelper.writeTx(TX_FILE, transactions); }
    private void saveAll(){ saveBooks(); saveTx(); }
}
