import org.junit.Assert.*;
import org.junit.Test;

import org.junit.jupiter.api.BeforeEach;
import project.Book;
import project.Library;
import project.User;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

public class UserTest {
    public User user;
    public Map<String,User> name;
    public Map<String,User> userId;
    public Map<String,User> borrowedBooks;

    @BeforeEach
    public void createUser() throws Exception{
        Field nameField = User.class.getDeclaredField("name");
        Field userIdField = User.class.getDeclaredField("userId");
        Field borrowedBooksField = User.class.getDeclaredField("borrowedBooks");

        nameField.setAccessible(true);
        userIdField.setAccessible(true);
        borrowedBooksField.setAccessible(true);

        var user = new User("Anthony", "user001");

        name = (Map<String,User>) nameField.get(user);
        userId = (Map<String,User>) userIdField.get(user);
        borrowedBooks = (Map<String,User>) borrowedBooksField.get(user);
    }

    @Test
    public void getNameTest() {
        assertSame(this.name, user.getName());
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
