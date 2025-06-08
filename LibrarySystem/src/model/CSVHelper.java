package model;

import java.io.*;
import java.util.*;

public class CSVHelper {

    public static List<Book> readBooksFromCSV(String filepath) {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                books.add(new Book(parts[0], parts[1], parts[2], Integer.parseInt(parts[3])));
            }
        } catch (IOException e) {
            System.err.println("Error reading books: " + e.getMessage());
        }
        return books;
    }

    public static void writeBooksToCSV(String filepath, List<Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (Book b : books) {
                bw.write(b.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing books: " + e.getMessage());
        }
    }

    public static List<Member> readMembersFromCSV(String filepath) {
        List<Member> members = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                members.add(new Member(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (IOException e) {
            System.err.println("Error reading members: " + e.getMessage());
        }
        return members;
    }

    public static void writeMembersToCSV(String filepath, List<Member> members) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (Member m : members) {
                bw.write(m.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing members: " + e.getMessage());
        }
    }

    public static List<Transaction> readTransactionsFromCSV(String filepath) {
        List<Transaction> txs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                txs.add(new Transaction(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions: " + e.getMessage());
        }
        return txs;
    }

    public static void writeTransactionsToCSV(String filepath, List<Transaction> txs) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (Transaction t : txs) {
                bw.write(t.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing transactions: " + e.getMessage());
        }
    }
}
