package dao;

import model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class CSVHelper {

    // ── Generic util ──────────────────────────────────────────────────
    private static BufferedReader reader(String path) throws IOException {
        return Files.newBufferedReader(Paths.get(path));
    }
    private static BufferedWriter writer(String path) throws IOException {
        return Files.newBufferedWriter(Paths.get(path));
    }

    /* ======================= BOOK ======================= */
    public static List<Book> readBooks(String path){
        List<Book> list = new ArrayList<>();
        try (BufferedReader br = reader(path)) {
            String line;
            while((line = br.readLine()) != null){
                String[] p = line.split(",", -1);
                if(p.length < 5) continue;
                list.add(new Book(p[0], p[1], p[2], p[3], Integer.parseInt(p[4])));
            }
        } catch (IOException ignored){}
        return list;
    }
    public static void writeBooks(String path, List<Book> books){
        try(BufferedWriter bw = writer(path)){
            for(Book b: books) bw.write(b.toCSV() + System.lineSeparator());
        } catch (IOException ignored){}
    }

    /* ======================= MEMBER ===================== */
    public static List<Member> readMembers(String path){
        List<Member> list = new ArrayList<>();
        try(BufferedReader br = reader(path)){
            String ln;
            while((ln = br.readLine()) != null){
                String[] p = ln.split(",", -1);
                if(p.length < 5) continue;
                list.add(new Member(p[0], p[1], p[2], p[3], p[4]));
            }
        } catch (IOException ignored){}
        return list;
    }
    public static void writeMembers(String path, List<Member> members){
        try(BufferedWriter bw = writer(path)){
            for(Member m: members) bw.write(m.toCSV() + System.lineSeparator());
        } catch (IOException ignored){}
    }

    /* =================== TRANSACTION ==================== */
    public static List<Transaction> readTx(String path){
        List<Transaction> list = new ArrayList<>();
        try(BufferedReader br = reader(path)){
            String ln;
            while((ln = br.readLine()) != null){
                String[] p = ln.split(",", -1);
                if(p.length < 7) continue;
                list.add(new Transaction(p));
            }
        } catch(IOException ignored){}
        return list;
    }
    public static void writeTx(String path, List<Transaction> tx){
        try(BufferedWriter bw = writer(path)){
            for(Transaction t: tx) bw.write(t.toCSV() + System.lineSeparator());
        } catch(IOException ignored){}
    }
}
