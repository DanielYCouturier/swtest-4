import org.junit.Assert.*;
import org.junit.Test;

import org.junit.jupiter.api.BeforeEach;
import project.Book;
import project.Library;
import project.User;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

public class BookTest {
    public Book book;
    public String title;
    public String author;
    public String isbn;
    public boolean isAvailable;

    @BeforeEach
    public void createBook() throws Exception {
        Field titleField = Book.class.getDeclaredField("title");
        Field authorField = Book.class.getDeclaredField("author");
        Field isbnField = Book.class.getDeclaredField("isbn");
        Field isAvailableField = Book.class.getDeclaredField("isAvailable");

        titleField.setAccessible(true);
        authorField.setAccessible(true);
        isbnField.setAccessible(true);
        isAvailableField.setAccessible(true);

        var book = new Book("Java Programming", "Author A", "ISBN123");

        title = (String) titleField.get(book);
        author = (String) authorField.get(book);
        isbn = (String) isbnField.get(book);
        isAvailable = (boolean) isAvailableField.get(book);
    }

    @Test
    public void getTitleTest() {
        assertSame(this.title, book.getTitle());
    }

    @Test
    public void getAuthorTest() {
        assertSame(this.author, book.getAuthor());
    }

    @Test
    public void getIsbnTest() {
        assertSame(this.isbn, book.getIsbn());
    }

    @Test
    public void isAvailableTest() {
        assertSame(this.isAvailable, book.isAvailable());
    }

    @Test
    public void borrowBookTest() {
        assertSame(this.isAvailable, book.isAvailable());
        book.borrowBook();
        assertSame(this.isAvailable, book.isAvailable());
    }

    @Test
    public void returnBookTest() {
        book.returnBook();
        assertSame(this.isAvailable, book.isAvailable());
    }

}