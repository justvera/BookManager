import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class BookManager {
    private ArrayList<Book> books = new ArrayList<>();
    private final String fileName;

    public BookManager(String fileName) {
        this.fileName = fileName;
        loadBooksFromFile();
    }

    private void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    int year = Integer.parseInt(parts[2].trim());
                    String genre = parts[3].trim();
                    books.add(new Book(title, author, year, genre));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooksToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Book book : books) {
                bw.write(book.getTitle() + "," + book.getAuthor() + "," + book.getYear() + "," + book.getGenre());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile();
    }

    public void removeBook(Book book) {
        books.remove(book);
        saveBooksToFile();
    }

    public ArrayList<Book> searchBooks(String regex) {
        ArrayList<Book> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        for (Book book : books) {
            if (pattern.matcher(book.getTitle()).find() ||
                pattern.matcher(book.getAuthor()).find() ||
                pattern.matcher(book.getGenre()).find()) {
                result.add(book);
            }
        }
        return result;
    }

    public ArrayList<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public void sortBooks(Comparator<Book> comparator) {
        Collections.sort(books, comparator);
    }
}
