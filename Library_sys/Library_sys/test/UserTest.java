import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import project.Book;
import project.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    public User user;
    public String name;
    public String userId;
    public List<Book> borrowedBooks;

    @BeforeEach
    public void createUser() throws Exception{
        Field nameField = User.class.getDeclaredField("name");
        Field userIdField = User.class.getDeclaredField("userId");
        Field borrowedBooksField = User.class.getDeclaredField("borrowedBooks");

        nameField.setAccessible(true);
        userIdField.setAccessible(true);
        borrowedBooksField.setAccessible(true);

        var user = new User("Anthony", "user001");

        name = (String) nameField.get(user);
        userId = (String) userIdField.get(user);
        borrowedBooks = (List<Book>) borrowedBooksField.get(user);
    }

    @Test
    public void getNameTest() {
        assertSame(name, user.getName());
    }

    @Test
    public void getUserIdTest() {
        assertSame(this.userId, user.getUserId());
    }

    @Test
    public void getBorrowedBooksTest() {
        assertSame(this.borrowedBooks, user.getBorrowedBooks());
    }

    @Test
    public void borrowedBookTest() {
        var book = new Book("Java Programming", "Author A", "ISBN123");
        assertTrue(book.isAvailable());
        user.borrowBook(book);
        assertTrue(user.getBorrowedBooks().contains(book));
    }

    @Test
    public void returnBookTest() {
        var book = new Book("Java Programming", "Author A", "ISBN123");
        assertTrue(book.isAvailable());
        user.borrowBook(book);
        assertTrue(user.getBorrowedBooks().contains(book));
        user.returnBook(book);
        assertFalse(user.getBorrowedBooks().contains(book));
    }
}
