import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

public class BookManagerGUI extends JFrame {
    private BookManager bookManager;
    private DefaultListModel<Book> bookListModel;
    private JList<Book> bookList;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;

    public BookManagerGUI(String fileName) {
        bookManager = new BookManager(fileName);
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        searchField = new JTextField(20);
        sortComboBox = new JComboBox<>(new String[] {"Title", "Author", "Year", "Genre"});
        
        setLayout(new BorderLayout());

        // Panel wyszukiwania
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Panel listy książek
        add(new JScrollPane(bookList), BorderLayout.CENTER);

        // Panel dodawania i usuwania książek
        JPanel managePanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        JButton removeButton = new JButton("Remove Book");
        managePanel.add(addButton);
        managePanel.add(removeButton);
        add(managePanel, BorderLayout.SOUTH);

        // Panel sortowania
        JPanel sortPanel = new JPanel();
        sortPanel.add(new JLabel("Sort by:"));
        sortPanel.add(sortComboBox);
        JButton sortButton = new JButton("Sort");
        sortPanel.add(sortButton);
        add(sortPanel, BorderLayout.EAST);

        // Akcje przycisków
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                ArrayList<Book> result = bookManager.searchBooks(query);
                updateBookList(result);
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog("Enter title:");
                String author = JOptionPane.showInputDialog("Enter author:");
                int year = Integer.parseInt(JOptionPane.showInputDialog("Enter year:"));
                String genre = JOptionPane.showInputDialog("Enter genre:");
                bookManager.addBook(new Book(title, author, year, genre));
                updateBookList(bookManager.getBooks());
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Book selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    bookManager.removeBook(selectedBook);
                    updateBookList(bookManager.getBooks());
                }
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String criteria = (String) sortComboBox.getSelectedItem();
                Comparator<Book> comparator = null;
                switch (criteria) {
                    case "Title":
                        comparator = Comparator.comparing(Book::getTitle);
                        break;
                    case "Author":
                        comparator = Comparator.comparing(Book::getAuthor);
                        break;
                    case "Year":
                        comparator = Comparator.comparingInt(Book::getYear);
                        break;
                    case "Genre":
                        comparator = Comparator.comparing(Book::getGenre);
                        break;
                }
                bookManager.sortBooks(comparator);
                updateBookList(bookManager.getBooks());
            }
        });

        updateBookList(bookManager.getBooks());

        setTitle("Book Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void updateBookList(ArrayList<Book> books) {
        bookListModel.clear();
        for (Book book : books) {
            bookListModel.addElement(book);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BookManagerGUI("books.txt");
            }
        });
    }
}