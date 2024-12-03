import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.Library;
import project.Book;
import project.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class IntegrationTest {
    public Library lib;

    public User user;
    public String name;
    public String userId;
    public List<Book> borrowedBooks;

    public Book book;
    public String title;
    public String author;
    public String isbn;
    public boolean isAvailable;

    public List<Book> lib_books;
    public Map<String, User> lib_users;

    @BeforeEach
    public void setUpLibrary() throws Exception {
        lib = new Library();

        user = new User("name", "userid");
        Field nameField = User.class.getDeclaredField("name");
        Field userIdField = User.class.getDeclaredField("userId");
        Field borrowedBooksField = User.class.getDeclaredField("borrowedBooks");

        nameField.setAccessible(true);
        userIdField.setAccessible(true);
        borrowedBooksField.setAccessible(true);

        name = (String) nameField.get(user);
        userId = (String) userIdField.get(user);
        borrowedBooks = (List<Book>) borrowedBooksField.get(user);

        book = new Book("title", "author", "isbn");
        Field titleField = Book.class.getDeclaredField("title");
        Field authorField = Book.class.getDeclaredField("author");
        Field isbnField = Book.class.getDeclaredField("isbn");
        Field isAvailableField = Book.class.getDeclaredField("isAvailable");

        titleField.setAccessible(true);
        authorField.setAccessible(true);
        isbnField.setAccessible(true);
        isAvailableField.setAccessible(true);

        title = (String) titleField.get(book);
        author = (String) authorField.get(book);
        isbn = (String) isbnField.get(book);
        isAvailable = (boolean) isAvailableField.get(book);

        lib.registerUser(user);
        lib.addBook(book);

        Field usersField = Library.class.getDeclaredField("users");
        usersField.setAccessible(true);
        lib_users = (Map<String, User>) usersField.get(lib);

        Field booksField = Library.class.getDeclaredField("books");
        booksField.setAccessible(true);
        lib_books = (List<Book>) booksField.get(lib);
    }

    @Test
    public void bookLibraryConsistency() {
        assertSame(isAvailable, book.isAvailable());
        lib.borrowBook(userId, isbn);

        var user2 = new User("name2", "userid2");
        lib.registerUser(user2);
        boolean error = false;
       try {
           lib.borrowBook(user2.getUserId(), isbn);
       }catch (Exception e) {
            error = true;
       }
       assertEquals(error,!book.isAvailable());
    }
    @Test
    public void bookLibraryConsistency2() {
        var isAvailable = book.isAvailable();
        var user2 = new User("name2", "userid2");
        lib.registerUser(user2);
        boolean error = false;
        try {
            lib.borrowBook(user2.getUserId(), isbn);
        }catch (Exception e) {
            error = true;
        }
        assertEquals(error,!isAvailable);
    }


}
