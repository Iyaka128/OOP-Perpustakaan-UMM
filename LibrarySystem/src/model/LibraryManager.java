package model;

import java.util.ArrayList;

public class LibraryManager {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public void borrowBook(String transactionId, String memberId, String isbn, String borrowDate, String returnDate) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn) && book.getQuantity() > 0) {
                book.setQuantity(book.getQuantity() - 1);
                transactions.add(new Transaction(transactionId, memberId, isbn, borrowDate, returnDate, "Borrowed"));
                break;
            }
        }
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
